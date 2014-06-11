/**
 * Created by dixit bhatta on 6/10/14.
 * This class is the GUI for the DES encryption algorithm
 */
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class desGUI {
    Frame frm;
    TextArea tOutput;
    Panel p;
    Panel p1;
    Label lInput;
    TextField tInput;
    Label lOutput;
    TextField tOput;
    Label lKey;
    TextField tKey;
    Button Encrypt;
    Button Decrypt;

    public desGUI(){
        frm=new Frame("DES Encryption");
        tOutput = new TextArea();
        frm.add(tOutput);
        frm.setSize(700,400);
        frm.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        p = new Panel();
        p1 = new Panel();
        lInput = new Label("Input (hex)");
        tInput = new TextField(20);
        lKey =new Label("Key (hex)");
        tKey=new TextField(20);
        lOutput = new Label("Output (hex)");
        tOput = new TextField(20);
        p.setLayout(new GridLayout(4,1));
        p.add(lInput);
        p.add(tInput);
        p.add(lKey);
        p.add(tKey);
        p.add(lOutput);
        p.add(tOput);
        Encrypt=new Button("Encrypt");
        p.add(Encrypt);
        Decrypt=new Button("Decrypt");
        p.add(Decrypt);
        p1.add(p);
        frm.add(p1,BorderLayout.NORTH);
    }
}
