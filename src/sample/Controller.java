package sample;

import com.sun.glass.ui.Cursor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    public VBox vboxLeft;
    public AnchorPane midPane;
    public VBox vboxRight;
    @FXML
    private VBox sideRight;
    @FXML
    private VBox sideLeft;


    @FXML
    private Canvas canvasLeft;
    @FXML
    private Canvas canvasRight;

    GraphicsContext context1;
    GraphicsContext context2;

    int r1 =(int) Math.round(Math.random()*1000000000);
    int r2 =(int) Math.round(Math.random()*1000000000);
    int tableSize=40;
    double step = 0;
    String[] T1 = new String[tableSize];
    String[] T2 = new String[tableSize];
    String x="";


    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        canvasLeft.heightProperty().bind(midPane.heightProperty());
        for(int i =1;i<tableSize;i++){
            vboxLeft.getChildren().add(new Label("Labela: "+i));
        }
        ((Label)vboxLeft.getChildren().get(1)).setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(5.0), null)));
        ((Label)vboxLeft.getChildren().get(1)).setPrefWidth(100);
        ((Label)vboxLeft.getChildren().get(1)).setMaxHeight(400);
        ((Label)vboxLeft.getChildren().get(3)).setPrefWidth(150);
        ((Label)vboxLeft.getChildren().get(3)).setMaxHeight(400);
        ((Label)vboxLeft.getChildren().get(3)).setText("01234567891234567890");
        ((Label)vboxLeft.getChildren().get(3)).setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(5.0), null)));
        context1 = canvasLeft.getGraphicsContext2D();

        context1.clearRect(0, 0, canvasLeft.getWidth(), canvasLeft.getHeight());
        context1.setFill(Color.TOMATO);
        context1.fillRect(0, 0, canvasLeft.getWidth(), canvasLeft.getHeight());}
        //context2 = canvasRight.getGraphicsContext2D();
/*


        
        for (var i=0;i<tableSize;i++){
            T1[i] = ""; T2[i] = "";
        }
        
        T1[hash("TEST1",1)] = "TEST1";
        T2[hash("TEST2",2)] = "TEST2";
// clear canvas
        context1.clearRect(0, 0, canvasLeft.getWidth(), canvasLeft.getHeight());
        context1.setFill(Color.TOMATO);
        context1.fillRect(0, 0, canvasLeft.getWidth(), canvasLeft.getHeight());

        context2.clearRect(0, 0, canvasRight.getWidth(), canvasRight.getHeight());
        context2.setFill(Color.TOMATO);
        context2.fillRect  (0, 0, canvasRight.getWidth(), canvasRight.getHeight());

// draw tables
        for (int i=0;i<tableSize;i++){
            context1.beginPath();
            context1.rect(4, 20+i*40, 160, 40);
            context1.setFill(Color.color(1,1,1));
            context1.fill();
            context1.setLineWidth(1);
            //context1.strokeStyle = '#555';
            context1.stroke();
        }

        for (int i=0;i<tableSize;i++){
            context2.beginPath();
            context2.rect(canvasRight.getWidth()-164, 20+i*40, 160, 40);
            context2.setFill(Color.color(1,1,1));
            context2.fill();
            context2.setLineWidth(1);
            //context2.setStroke(); = '#555';
            context2.stroke();
        }

        /*context1.beginPath();
        context1.setFill(Color.color(0,0,0));
        //context1.setFont(Font);
        context1.fillText("T1",65,460);

        context2.beginPath();
        context2.setFill(Color.color(0,0,0));
        //context2.font='25px Georgia';
        context2.fillText("T2",195,460);*/
/*
        disparrays();
    }


    public void mouseClicked(MouseEvent mouseEvent) {
        System.out.println("Clicked on Canvas");

        //drawShapes(graphicsContext);
    }






    void disparrays(){
        for (int i=0;i<tableSize;i++){
            context1.beginPath();
            context1.setFill(Color.color(1,1,1));
            context1.fillRect(4+2, 20+i*40+2, 160-4, 40-4);
            context1.fill();
            context1.setFill(Color.color(0,0,0));
            //context1.font='15px Courier';
            context1.fillText(T1[i],10,43+i*40);

            context2.beginPath();
            context2.setFill(Color.color(1,1,1));
            context2.fillRect(canvasRight.getWidth()-160, 20+i*40+2, 160-4, 40-4);
            context2.fill();
            context2.setFill(Color.color(0,0,0));
            //context2.font='15px Courier';
            context2.fillText(T2[i],canvasRight.getWidth()-158,43+i*40);
        }

    }

    // Initialize variables


//document.getElementById('inputx').focus();

// ------------------- functions

    // draw arcs and arrows
    void arrow(int pos,int side,int bidir,int width){
        GraphicsContext ctx = (side==1)?context1:context2;

        ctx.beginPath();
        ctx.setFill(Color.color(1,1,1));
        if (side==2)
            ctx.fillRect  (0, 0, 134, canvasRight.getHeight());
        else
            ctx.fillRect  (166, 0, canvasLeft.getWidth()-166, canvasLeft.getHeight());
        ctx.fill();

        ctx.beginPath();
        if (side==2){
            ctx.moveTo(134,40*(pos+1));
            ctx.lineTo(126,40*(pos+1)-8);
            ctx.lineTo(126,40*(pos+1)+8);
            ctx.setFill(Color.color(0,0,0));
            ctx.fill();
            if (bidir==1){
                ctx.moveTo(0,230);
                ctx.lineTo(8,222);
                ctx.lineTo(8,238);
                ctx.setFill(Color.color(0,0,0));
                ctx.fill();
            }
        } else {
            ctx.moveTo(167,40*(pos+1));
            ctx.lineTo(174,40*(pos+1)-8);
            ctx.lineTo(174,40*(pos+1)+8);
            ctx.setFill(Color.color(0,0,0));
            ctx.fill();
            if (bidir==1){
                ctx.moveTo(300,230);
                ctx.lineTo(292,222);
                ctx.lineTo(292,238);
                ctx.setFill(Color.color(0,0,0));
                ctx.fill();
            }
        }

        ctx.beginPath();
        ctx.setLineWidth(1);
        if (side==2){
            ctx.moveTo(3,230);
            ctx.bezierCurveTo(40,230,80,40*(pos+1),132,40*(pos+1));
        } else {
            ctx.moveTo(174,40*(pos+1));
            ctx.bezierCurveTo(220,40*(pos+1),260,230,297,230);
        }
        ctx.stroke();
    }



/*

    function clearfield(){
        context1.beginPath();
        context1.fillStyle = '#fff';
        context1.fillRect  (166, 0, canvasLeft.getWidth()-166, canvasLeft.getHeight());
        context1.fill();

        context2.beginPath();
        context2.fillStyle = '#fff';
        context2.fillRect  (0, 0, 134, canvasRight.getHeight());
        context2.fill();
    }

    function highlight(side, i, cond){
        if (side==1){
            context1.beginPath();
            context1.fillStyle = cond?"rgba(0, 255, 0, 0.5)":"rgba(255, 0, 0, 0.5)";
            context1.fillRect(4+2, 20+i*40+2, 160-4, 40-4);
        } else {
            context2.beginPath();
            context2.fillStyle = cond?"rgba(0, 255, 0, 0.5)":"rgba(255, 0, 0, 0.5)";
            context2.fillRect(136+2, 20+i*40+2, 160-4, 40-4);
        }

    }

    // process change in input box
    function process(){
        x = document.getElementById("inputx").value;
        clearfield();
        if (x.length>0){
            x1 = hash(x, 1);
            x2 = hash(x, 2);
            arrow(x1, 1, 0, 1);
            arrow(x2, 2, 0, 1);

            disparrays();
            highlight(1,x1,(T1[x1] == x));
            highlight(2,x2,(T2[x2] == x));

            if ((T1[x1] == x) || (T2[x2] == x)){
                document.getElementById("message").innerHTML = "<font color='green'>FOUND</font>";
            } else {
                document.getElementById("message").innerHTML = "<font color='red'>NOT FOUND</font>";
            }
        } else {
            document.getElementById("message").innerHTML = "";
            disparrays();
        }
        document.getElementById('inputx').focus();
    }



    function insrt(loop){

        if (loop > 16){
            document.getElementById("message").innerHTML = "<font color='red'>INSERT fail.<br><br> MaxLoop=16 <br>reached. <br>Should rehash,<br>and increase<br>table size.</font>";
            setTimeout("document.getElementById('inputx').readOnly = false;document.getElementById('insertbutton').disabled = false;document.getElementById('deletebutton').disabled = false;document.getElementById('inputx').focus();",2000);
            return;
        }
        x = document.getElementById("inputx").value;
        if (x.length>0){
            x1 = hash(x, 1);
            x2 = hash(x, 2);
            document.getElementById('inputx').style.backgroundColor='white';
            if ((T1[x1] == x) || (T2[x2] == x)){
                document.getElementById("message").innerHTML = "<font color='red'>INSERT fail.<br><br> EXISTS.</font>";
                setTimeout("document.getElementById('inputx').focus();", 1000);
            }	else if (T1[x1] == ''){
                T1[x1] = x;
                disparrays();
                clearfield();
                arrow(x1,1,0,3);
                highlight(1,x1,true);
                document.getElementById("message").innerHTML = "<font color='green'>INSERT ok.</font>";
                document.getElementById('inputx').value = '';
                setTimeout("document.getElementById('inputx').readOnly = false;document.getElementById('insertbutton').disabled = false;document.getElementById('deletebutton').disabled = false;document.getElementById('inputx').focus();", 1000);
            } else if (T1[x1] != x){
                disparrays();
                clearfield();
                arrow(x1,1,0,3);
                highlight(1,x1,false);
                document.getElementById("message").innerHTML = "<font color='green'>Cell occupied.<br><br> We evict. <br><br> (Loop: "+loop+"/16)</font>";
                document.getElementById('inputx').readOnly = true;
                document.getElementById("insertbutton").disabled = true;
                document.getElementById("deletebutton").disabled = true;
                setTimeout("arrow("+x1+",1,1,3);", 1000);
                setTimeout("document.getElementById('inputx').value = '"+T1[x1]+"';T1["+x1+"]='"+x+"';disparrays();document.getElementById('inputx').style.backgroundColor='#7fff7f';highlight(1,"+x1+",true);", 2000);
                setTimeout("insertright("+(loop+1)+");", 3000);

            }
        }
    }


    function insertright(loop){
        x = document.getElementById("inputx").value;
        if (x.length>0){
            x1 = hash(x, 1);
            x2 = hash(x, 2);
            document.getElementById('inputx').style.backgroundColor='white';
            if (T2[x2] == ''){
                T2[x2] = x;
                disparrays();
                clearfield();
                arrow(x2,2,0,3);
                highlight(2,x2,true);
                document.getElementById("message").innerHTML = "<font color='green'>INSERT ok.</font>";
                document.getElementById('inputx').value = '';
                setTimeout("document.getElementById('inputx').readOnly = false;document.getElementById('insertbutton').disabled = false;document.getElementById('deletebutton').disabled = false; document.getElementById('inputx').focus();", 1000);
            } else if (T2[x2] != x){
                disparrays();
                clearfield();
                arrow(x2,2,0,3);
                highlight(2,x2,false);
                document.getElementById("message").innerHTML = "<font color='green'>Cell occupied.<br><br> We evict. <br><br> (Loop: "+loop+"/16)</font>";
                document.getElementById('inputx').readOnly = true;
                document.getElementById("insertbutton").disabled = true;
                document.getElementById("deletebutton").disabled = true;
                setTimeout("arrow("+x2+",2,1,3);", 1000);
                setTimeout("document.getElementById('inputx').value = '"+T2[x2]+"';T2["+x2+"]='"+x+"';disparrays();;document.getElementById('inputx').style.backgroundColor='#7fff7f';;highlight(2,"+x2+",true);", 2000);
                setTimeout("insrt("+(loop+1)+");", 3000)
            }
        }
    }

    function delt(){
        x = document.getElementById("inputx").value;
        if (x.length>0){
            x1 = hash(x, 1);
            x2 = hash(x, 2);
            if ((T1[x1] != x) && (T2[x2] != x)){
                document.getElementById("message").innerHTML = "<font color='red'>DELETE fail.<br><br> NOT FOUND</font>";
                setTimeout("document.getElementById('inputx').focus();", 1000);
            }  else if (T1[x1] == x){
                T1[x1] = '';
                disparrays();
                document.getElementById("message").innerHTML = "<font color='green'>DELETE ok.</font>";
                setTimeout("document.getElementById('inputx').value = '';document.getElementById('inputx').focus();", 1000);
            }  else if (T2[x2] == x){
                T2[x2] = '';
                disparrays();
                document.getElementById("message").innerHTML = "<font color='green'>DELETE ok.</font>";
                setTimeout("document.getElementById('inputx').value = '';document.getElementById('inputx').focus();", 1000);
            }
        }

    }*/

    // dummy hash function loosely based on http://stackoverflow.com/questions/7616461/generate-a-hash-from-string-in-javascript-jquery
// note: this is fine for visualization, but not for any real application
    int hash(String stri,int variant) {
        int hash = 0, i, chr, len;
        if (stri.length() == 0) return hash;
        for (i = 0, len = stri.length(); i < len; i++) {
            chr   = stri.charAt(i);
            hash  = ((hash << 5) - hash) + chr + (chr*((variant==1)?r1:r2)<<i);
            hash |= 0;
        }
        hash = Math.abs(hash) % tableSize;
        return hash;
    }


    public void mouseClicked(MouseEvent mouseEvent) {context1.clearRect(0, 0, canvasLeft.getWidth(), canvasLeft.getHeight());
        context1.setFill(Color.TOMATO);
        context1.fillRect(0, 0, canvasLeft.getWidth(), canvasLeft.getHeight());
        System.out.println(canvasLeft.getHeight()+"     "+canvasLeft.getWidth());
        System.out.println(vboxLeft.getHeight()+"     "+vboxLeft.getWidth());
    }

    public void delHalf(ActionEvent actionEvent) {
        tableSize*=2;
        for(int i =tableSize/2;i<tableSize;i++){
            vboxLeft.getChildren().add(new Label("Labela nova: "+i));
        }

       /* try {
            //Thread.sleep(1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        this.mouseClicked(null);
    }
}
