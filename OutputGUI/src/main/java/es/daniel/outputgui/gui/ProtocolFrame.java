package es.daniel.outputgui.gui;

import es.daniel.outputgui.data.Bucket;
import es.daniel.outputgui.data.DataManagerListener;
import org.jfree.chart.ChartPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProtocolFrame extends JPanel  implements DataManagerListener {
    DetectedPanel detectedPanel;
    TimeDetectedPanel timeDetectedPanel;

    JButton btnClear;

    public ProtocolFrame(String protocol) {
        super();
        setLayout(new GridBagLayout());
        detectedPanel = new DetectedPanel();
        timeDetectedPanel= new TimeDetectedPanel();
        btnClear = new JButton("Clear");
        GridBagConstraints c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        add(new JLabel(protocol), c);
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=1;
        c.weightx=1;
        c.weighty=1;
        c.fill=GridBagConstraints.BOTH;
        add(detectedPanel,c);
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=2;
        c.weightx=1;
        c.weighty=1;
        c.fill=GridBagConstraints.BOTH;
        add(timeDetectedPanel,c);

        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=3;
        add(btnClear,c);

        //setPreferredSize(new Dimension(500, 300));

        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                detectedPanel.clearAllData();
                timeDetectedPanel.clearAllData();
            }
        });
    }

    public void addOrUpdateBucket(Bucket bucket) {
        detectedPanel.addData(bucket);
        timeDetectedPanel.addData(bucket);
    }
}
