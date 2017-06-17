function varargout = DataAnalisys(varargin)
% DATAANALISYS MATLAB code for DataAnalisys.fig
%      DATAANALISYS, by itself, creates a new DATAANALISYS or raises the existing
%      singleton*.
%
%      H = DATAANALISYS returns the handle to a new DATAANALISYS or the handle to
%      the existing singleton*.
%
%      DATAANALISYS('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in DATAANALISYS.M with the given input arguments.
%
%      DATAANALISYS('Property','Value',...) creates a new DATAANALISYS or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before DataAnalisys_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to DataAnalisys_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help DataAnalisys

% Last Modified by GUIDE v2.5 14-Jun-2017 18:43:56

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @DataAnalisys_OpeningFcn, ...
                   'gui_OutputFcn',  @DataAnalisys_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT


% --- Executes just before DataAnalisys is made visible.
function DataAnalisys_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to DataAnalisys (see VARARGIN)

% Choose default command line output for DataAnalisys
handles.output = hObject;
handles.packets = readDataCsv('data-testPattern.csv');

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes DataAnalisys wait for user response (see UIRESUME)
% uiwait(handles.figure1);
plotData(handles);

% --- Outputs from this function are returned to the command line.
function varargout = DataAnalisys_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;


% --- Executes on slider movement.
function sldMax_Callback(hObject, eventdata, handles)
% hObject    handle to sldMax (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'Value') returns position of slider
%        get(hObject,'Min') and get(hObject,'Max') to determine range of slider
max =get(hObject,'Value');
min = get(handles.sldMin,'Value');
limit = max-32;
if(min>limit)
    set(handles.sldMin,'Value',limit);
end
plotData(handles)


% --- Executes during object creation, after setting all properties.
function sldMax_CreateFcn(hObject, eventdata, handles)
% hObject    handle to sldMax (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: slider controls usually have a light gray background.
if isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor',[.9 .9 .9]);
end


% --- Executes on slider movement.
function sldMin_Callback(hObject, eventdata, handles)
% hObject    handle to sldMin (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'Value') returns position of slider
%        get(hObject,'Min') and get(hObject,'Max') to determine range of slider
max =get(handles.sldMax,'Value');
min = get(hObject,'Value');
limit = min+32;
if(max<limit)
    set(handles.sldMax,'Value',limit);
end
plotData(handles)

% --- Executes during object creation, after setting all properties.
function sldMin_CreateFcn(hObject, eventdata, handles)
% hObject    handle to sldMin (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: slider controls usually have a light gray background.
if isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor',[.9 .9 .9]);
end

% --- Uptdates the plots
function plotData(handles)
% handles    structure with handles and user data (see GUIDATA)
axes(handles.axData);
max =int16(get(handles.sldMax,'Value'));
min = int16(get(handles.sldMin,'Value'));
packet = handles.packets(1);
time = packet.data(min:max,1);
a0=packet.data(min:max,3);
a1=packet.data(min:max,4);
plot(time,a0, time,a1);

Fs = 1000;            % Sampling frequency                    
L = max-min;             % Length of signal
Y0 = fft(double(a0));
Y0= fftshift(Y0);
P0 = abs(Y0/double(L));
Y1=fft(double(a1));
Y1= fftshift(Y1);
P1 = abs(Y1/double(L));


f = (-L/2:L/2-1)*(Fs/L);
sf = size(f);
sf=sf(2);
sp = size(P0);
sp=sp(1);
if sf ~= sp
    P0=P0(1:sf);
    P1=P1(1:sf);
end
axes(handles.axFft);
plot(f,P0,f,P1); 

