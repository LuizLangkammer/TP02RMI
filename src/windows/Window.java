package windows;

import classes.Client;
import classes.Field;
import classes.FieldInfo;
import rmi.ClientRMI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame implements ActionListener {

    JLabel titleLB = new JLabel("Batalha Naval");
    JLabel turnMessageLB = new JLabel("Aguardando Turno...");
    JLabel player1LB = new JLabel("Você");
    JLabel player2LB = new JLabel("Jogador 2");


    Font font= new Font("Times new Roman",Font.BOLD,30);
    Font font2= new Font("Times new Roman",Font.BOLD,20);

    Color black= new Color(0,0,0);

    JPanel divider = new JPanel();


    int lines, columns;

    Field[][] player1Fields;
    Field[][] player2Fields;

    ClientRMI client;


    public Window(FieldInfo[][] p1Informations, ClientRMI client){

        this.client = client;

        lines = p1Informations.length;
        columns = p1Informations[0].length;

        player1Fields = new Field[lines][columns];
        player2Fields = new Field[lines][columns];

        for(int i=0; i<lines; i++){
            for(int j=0; j<columns; j++){
                player1Fields[i][j] = new Field(p1Informations[i][j], i+"/"+j);
                player2Fields[i][j] = new Field(new FieldInfo(), i+"/"+j);
            }
        }

        setLayout(null);

        add(titleLB);
        titleLB.setFont(font);
        titleLB.setBounds(410,20,450,30);

        add(turnMessageLB);
        turnMessageLB.setFont(font2);
        turnMessageLB.setBounds(380,60,450,30);


        add(player1LB);
        player1LB.setFont(font2);
        player1LB.setBounds(220,145,80,30);

        add(divider);
        divider.setBackground(black);
        divider.setBounds(498,165,4,335);

        add(player2LB);
        player2LB.setFont(font2);
        player2LB.setBounds(700,145,150,30);

        for(int i=0; i<lines; i++){
            for(int j=0; j<columns; j++){

                JButton button = player1Fields[i][j].getButton();

                add(button);
                button.setBounds(50+(j*40),175+(i*40),40,40);


                button = player2Fields[i][j].getButton();
                add(button);
                button.setBounds(550+(j*40),175+(i*40),40,40);
                button.addActionListener(this);
            }
        }


        setTitle("udp.Client");
        setSize(1000,550);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton)e.getSource();
        String name = button.getName();
        String[] coordinates = name.split("/");
        client.play(Byte.parseByte(coordinates[0]), Byte.parseByte(coordinates[1]));
    }

    public void setMessage (String title){
        turnMessageLB.setText(title);
    }

    public void setField(byte i, byte j, boolean hit){
        player2Fields[i][j].setOpen(hit);
    }

    public void setField(byte i, byte j){
        player1Fields[i][j].setOpen();
    }



}
