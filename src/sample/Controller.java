package sample;

import com.sun.glass.ui.Cursor;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    public VBox vboxLeft;
    public AnchorPane midPane;
    public VBox vboxRight;
    public TextField inputField;
    public Line lineBato;
    public ScrollPane scroll;
    @FXML
    private VBox sideRight;
    @FXML
    private VBox sideLeft;
    private final double inputXStart=443.0;
    private double inputYStart=466.0;


    GraphicsContext context1;
    GraphicsContext context2;

    int r1 =(int) Math.round(Math.random()*1000000000);
    int r2 =(int) Math.round(Math.random()*1000000000);
    int tableSize=15;
    double step = 0;
    ArrayList<String> T1 = new ArrayList<>();
    ArrayList<String> T2 = new ArrayList<>();


    public void initializeTables(double times){

        tableSize*=times;
        //inputField.requestFocus();
        //inputField.setText("");

        // Requesting focus in separate Thread because at the time of initialize() controls are not yet ready to handle focus.
        Platform.runLater(() -> inputField.requestFocus());

        for(int i =0;i<tableSize;i++){
            Label label=new Label("");
            label.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-wrap-text:true; -fx-border-color:black;");
            label.setPadding(new Insets(5));
            label.setAlignment(Pos.CENTER);
            label.setMaxWidth(Double.MAX_VALUE);
            label.setTextAlignment(TextAlignment.CENTER);
            T1.add("");

            vboxLeft.getChildren().add(label);
        }

        for(int i =0;i<tableSize;i++){
            Label label=new Label("");
            label.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-wrap-text:true; -fx-border-color:black;");
            label.setPadding(new Insets(5));
            label.setAlignment(Pos.CENTER);
            label.setMaxWidth(Double.MAX_VALUE);
            label.setTextAlignment(TextAlignment.CENTER);
            T2.add("");

            vboxRight.getChildren().add(label);
        }

    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeTables(1);
        putKey(hash("TEST1",1),"TEST1",true);
        putKey(hash("TEST2",2),"TEST2",false);
        // disparrays();

        //((Label)vboxLeft.getChildren().get(3)).setPrefWidth(150);
        //((Label)vboxLeft.getChildren().get(3)).setMaxHeight(400);

    }
    public String putKey(int index, String key, boolean sideLeft){
        String oldKey=sideLeft?T1.remove(index): T2.remove(index);
        if(sideLeft){
            T1.add(index,key);
            ((Label)vboxLeft.getChildren().get(index)).setText(key);
        }else{
            T2.add(index,key);
            ((Label)vboxRight.getChildren().get(index)).setText(key);
        }

        return oldKey;
    }

    public String removeKey(String key){

            int x1 = hash(key, 1);
            int x2 = hash(key, 2);
            //draw arrows
            //arrow(x1, 1, 0, 1);
            //arrow(x2, 2, 0, 1);

            //obrisi sve hajlajtovano
            refreshArrays();
            if(T1.get(x1).equals(key)){
                vboxLeft.getChildren().remove(x1);
                return T1.remove(x1);
            }
            if(T2.get(x2).equals(key)){
                vboxRight.getChildren().remove(x2);
                return T2.remove(x2);
            }
            return null;
        }
    // process change in input box
    public void inputChange() {
        String input = inputField.getText();
        //clearfield(); just disable arrows
        if (input.length() > 0) {
            int x1 = hash(input, 1);
            int x2 = hash(input, 2);
            //draw arrows
            //arrow(x1, 1, 0, 1);
            //arrow(x2, 2, 0, 1);

            //obrisi sve hajlajtovano
            refreshArrays();

            // osenci sta treba
            highlight(true,x1,(T1.get(x1).equals(input)));
            highlight(false,x2,(T2.get(x2).equals(input)));

            /*if ((T1[x1] == x) || (T2[x2] == x)){
                document.getElementById("message").innerHTML = "<font color='green'>FOUND</font>";
            } else {
                document.getElementById("message").innerHTML = "<font color='red'>NOT FOUND</font>";
            }*/
        } else {
            //document.getElementById("message").innerHTML = "";
            refreshArrays();
        }
        inputField.requestFocus();

    }



    static boolean toggle=true;
    private void refreshArrays() {
        System.out.println("usao asdasd awqeq ");
        if(toggle){
        ObservableList<Node> children = vboxLeft.getChildren();
        for (Node l:children){
            ((Label) l).setBackground(new Background(new BackgroundFill(Color.AZURE, new CornerRadii(5.0), null)));
        }
        children = vboxRight.getChildren();
        for (Node l:children){
            ((Label) l).setBackground(new Background(new BackgroundFill(Color.AZURE, new CornerRadii(5.0), null)));
        }
        //toggle=false;
        }else{
            ObservableList<Node> children = vboxLeft.getChildren();
            for (Node l:children){
                ((Label) l).setBackground(new Background(new BackgroundFill(Color.TOMATO, new CornerRadii(5.0), null)));
            }
            children = vboxRight.getChildren();
            for (Node l:children){
                ((Label) l).setBackground(new Background(new BackgroundFill(Color.TOMATO, new CornerRadii(5.0), null)));
            }
            toggle=true;
        }
    }

    public void highlight(boolean leftSide,int index, boolean successful){
        if (leftSide){
            ((Label)vboxLeft.getChildren().get(index)).setBackground(new Background(new BackgroundFill(successful?Color.GREEN:Color.RED, new CornerRadii(5.0), null)));
        } else {
            ((Label)vboxRight.getChildren().get(index)).setBackground(new Background(new BackgroundFill(successful?Color.GREEN:Color.RED, new CornerRadii(5.0), null)));
        }

    }

    public void delete(){
        String input = inputField.getText();

        if (input.length() > 0) {
            removeKey(input);
        }
    }
/*
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


    public void mouseClicked(MouseEvent mouseEvent) {
        //context1.clearRect(0, 0, canvasLeft.getWidth(), canvasLeft.getHeight());
        //context1.setFill(Color.AZURE);
        //context1.fillRect(0, 0, canvasLeft.getWidth(), canvasLeft.getHeight());
        //System.out.println(canvasLeft.getHeight()+"     "+canvasLeft.getWidth());
        //System.out.println(vboxLeft.getHeight()+"     "+vboxLeft.getWidth());

        drawLine();



    }

    public void drawLine(){//boolean left, int index
        lineBato.setStartX(((Label) vboxLeft.getChildren().get(0)).getWidth());
        lineBato.setStartY(((Label) vboxLeft.getChildren().get(0)).getHeight()*(0.5+3));
        lineBato.setEndX(inputXStart);
        lineBato.setEndY(inputYStart+inputField.getHeight()*0.5);
    }

    public void delHalf(ActionEvent actionEvent) {
        initializeTables(2);
       /* for(int i =tableSize/2;i<tableSize;i++){
            //vboxLeft.getChildren().add(new Label("Labela nova: "+i));
            putKey(i,"Labela"+i,true);
        }*/
        this.mouseClicked(null);
    }

    public void scrollStart(ScrollEvent scrollEvent) {

    }

    public void scrollEnd(ScrollEvent scrollEvent) {
        drawLine();
    }

    public void scroll(ScrollEvent scrollEvent) {

       /* try {
            System.out.println("usao jee");
            inputYStart-=scrollEvent.getDeltaY();
            System.out.println("pomeraj je: "+inputYStart);
            System.out.println("pomeraj za vbox je "+vboxLeft.getLayoutX()+" asdasd"+vboxLeft.getLayoutY() );

            drawLine();
        }catch (Exception e){

        }*/

    }
}
