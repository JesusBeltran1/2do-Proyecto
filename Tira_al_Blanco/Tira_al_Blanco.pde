import shiffman.box2d.*;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.joints.*;
import org.jbox2d.collision.shapes.*;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.*;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.*;

Box2DProcessing box2d;
Flecha flecha;
Boundary boundary;
Boundary2 boundary2;
Arco arco;
Diana diana;

PImage pantalla_de_inicio, escenario_de_tiro, forma_del_arco, forma_de_la_diana, forma_de_la_flecha, pantalla_de_fin;
boolean tiro = false;
int pantalla = 0;

void setup() {
  size(1360, 456);
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
  box2d.setGravity(0.0,-10.0);
  
  flecha = new Flecha();
  boundary = new Boundary();
  boundary2 = new Boundary2();
  arco = new Arco();
  diana = new Diana();

  pantalla_de_inicio = loadImage("Pantalla de Inicio de Tira al Blanco.png");
  escenario_de_tiro = loadImage("Escenario de Tiro.png");
  forma_del_arco = loadImage("Arco.png");
  forma_de_la_diana = loadImage("Diana.png");
  forma_de_la_flecha = loadImage("Flecha.png");
  pantalla_de_fin = loadImage("Pantalla de Fin de Tira al Blanco.png");
}

void draw() {  
  box2d.step();
  switch(pantalla) {
  case 0:
  inicio();
  break;
  
  case 1:
  escenariodetiro();
  break;
  
  case 2:
  fin();
  break;
  }
}

void inicio() {
pushMatrix();
image(pantalla_de_inicio,0,0,1360, 456);
popMatrix();
}

void escenariodetiro() {
pushMatrix();
image(escenario_de_tiro, 0, 0, 1360, 456);
  boundary.display();
  boundary2.display();
  arco.display();
  diana.display();
  image(forma_del_arco , 33, 190, 136.55, 188.33);
  image(forma_de_la_diana, 1160, 65, 81, 81);
  flecha.display();
popMatrix();
}

void fin() {
pushMatrix();
image(pantalla_de_fin, 0, 0, 1360, 456);
popMatrix();
}

void mousePressed(){
  if (mouseButton == LEFT && pantalla==1) {
    arco.move(new Vec2(mouseX,mouseY));
    flecha.angular(0.5);
  }
}
  
void keyReleased() {
  if((key == 'a' || key == 'A') && pantalla==0){
    pantalla=1;
  } else if((key == 'b' || key == 'B') && pantalla==1){
    pantalla=2;
  } else if((key == 'c' || key == 'C') && pantalla==2){
    exit();
  }
}
  
void beginContact(Contact cp) {

  Fixture f1 = cp.getFixtureA();
  Fixture f2 = cp.getFixtureB();

  Body b1 = f1.getBody();
  Body b2 = f2.getBody();

  Object o1 = b1.getUserData();
  Object o2 = b2.getUserData(); 
}

void endContact(Contact cp) {
}
