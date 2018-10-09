import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import shiffman.box2d.*; 
import org.jbox2d.common.*; 
import org.jbox2d.dynamics.joints.*; 
import org.jbox2d.collision.shapes.*; 
import org.jbox2d.collision.shapes.Shape; 
import org.jbox2d.common.*; 
import org.jbox2d.dynamics.*; 
import org.jbox2d.dynamics.contacts.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Tira_al_Blanco extends PApplet {










Box2DProcessing box2d;
Flecha flecha;
Boundary boundary;
Boundary2 boundary2;
Arco arco;
Diana diana;

PImage pantalla_de_inicio, escenario_de_tiro, forma_del_arco, forma_de_la_diana, forma_de_la_flecha, pantalla_de_fin;
boolean tiro = false;
int pantalla = 0;

public void setup() {
  
  box2d = new Box2DProcessing(this);
  box2d.createWorld();
  box2d.setGravity(0.0f,-10.0f);
  
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

public void draw() {  
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

public void inicio() {
pushMatrix();
image(pantalla_de_inicio,0,0,1360, 456);
popMatrix();
}

public void escenariodetiro() {
pushMatrix();
image(escenario_de_tiro, 0, 0, 1360, 456);
  boundary.display();
  boundary2.display();
  arco.display();
  diana.display();
  image(forma_del_arco , 33, 190, 136.55f, 188.33f);
  image(forma_de_la_diana, 1160, 65, 81, 81);
  flecha.display();
popMatrix();
}

public void fin() {
pushMatrix();
image(pantalla_de_fin, 0, 0, 1360, 456);
popMatrix();
}

public void mousePressed(){
  if (mouseButton == LEFT && pantalla==1) {
    arco.move(new Vec2(mouseX,mouseY));
    flecha.angular(0.5f);
  }
}
  
public void keyReleased() {
  if((key == 'a' || key == 'A') && pantalla==0){
    pantalla=1;
  } else if((key == 'b' || key == 'B') && pantalla==1){
    pantalla=2;
  } else if((key == 'c' || key == 'C') && pantalla==2){
    exit();
  }
}
  
public void beginContact(Contact cp) {

  Fixture f1 = cp.getFixtureA();
  Fixture f2 = cp.getFixtureB();

  Body b1 = f1.getBody();
  Body b2 = f2.getBody();

  Object o1 = b1.getUserData();
  Object o2 = b2.getUserData(); 
}

public void endContact(Contact cp) {
}
class Arco { 
  Body body;
  int r = 16;
  Vec2 vel = new Vec2(10,0);
  boolean thrusting = false;

  Arco() {
    makeBody(new Vec2(30,190));
    body.setUserData(this);
  } 
  
  public void makeBody(Vec2 position) {
    Vec2[] vertices = new Vec2[4];
    vertices[0] = box2d.vectorPixelsToWorld(new Vec2(r,0));
    vertices[1] = box2d.vectorPixelsToWorld(new Vec2(r,r/2));
    vertices[2] = box2d.vectorPixelsToWorld(new Vec2(-r,r/2));
    vertices[3] = box2d.vectorPixelsToWorld(new Vec2(-r,0));

    PolygonShape ps = new PolygonShape();
    ps.set(vertices, vertices.length);

    BodyDef bd = new BodyDef();
    bd.type = BodyType.KINEMATIC;
    bd.position.set(box2d.coordPixelsToWorld(position));
   
    body = box2d.createBody(bd);
    
    body.createFixture(ps, 1.0f);
  }
  
  public void move(Vec2 vel){
      body.setLinearVelocity(vel);
    }

  public void display() { 
    Vec2 pos = box2d.getBodyPixelCoord(body);
    float a = body.getAngle();
    Fixture f = body.getFixtureList();
    PolygonShape ps = (PolygonShape) f.getShape();
    
    rectMode(CENTER);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(-a);
    noStroke();
    strokeWeight(2);
    noFill();
    beginShape();
    for (int i = 0; i < ps.getVertexCount(); i++) {
      Vec2 v = box2d.vectorWorldToPixels(ps.getVertex(i));
      vertex(v.x, v.y);
    }
    endShape(CLOSE);
    popMatrix();
  }
}
class Boundary { 
  Body body;
  int r = 16;
  boolean thrusting = false;

  Boundary() {
    makeBody(new Vec2(90,75),0,new Vec2(0, 0),0);
    body.setUserData(this);
  } 
  
  public void makeBody(Vec2 position, float angle, Vec2 vel,float omega) {
    Vec2[] vertices = new Vec2[4];
    vertices[0] = box2d.vectorPixelsToWorld(new Vec2(-100,height/3));
    vertices[1] = box2d.vectorPixelsToWorld(new Vec2(-100,height/2));
    vertices[2] = box2d.vectorPixelsToWorld(new Vec2(50,height/2));
    vertices[3] = box2d.vectorPixelsToWorld(new Vec2(50,height/3));
 
    PolygonShape ps = new PolygonShape();
    ps.set(vertices, vertices.length);

    BodyDef bd = new BodyDef();
    bd.type = BodyType.STATIC;
    bd.position.set(box2d.coordPixelsToWorld(position));
   
    bd.angle = angle; 
    
    body = box2d.createBody(bd);
    
    body.createFixture(ps, 1.0f);
    body.setLinearVelocity(vel);
    body.setAngularVelocity(omega);
  }

  public void display() { 
    Vec2 pos = box2d.getBodyPixelCoord(body);
    float a = body.getAngle();
    Fixture f = body.getFixtureList();
    PolygonShape ps = (PolygonShape) f.getShape();
    
    rectMode(CENTER);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(-a);
    noStroke();
    strokeWeight(2);
    noFill();
    beginShape();
    for (int i = 0; i < ps.getVertexCount(); i++) {
      Vec2 v = box2d.vectorWorldToPixels(ps.getVertex(i));
      vertex(v.x, v.y);
    }
    endShape(CLOSE);
    popMatrix();
  }
}
class Boundary2 { 
  Body body;
  int r = 16;
  boolean thrusting = false;

  Boundary2() {
    makeBody(new Vec2(width/2,281),0,new Vec2(0, 0),0);
    body.setUserData(this);
  } 
  
  public void makeBody(Vec2 position, float angle, Vec2 vel,float omega) {
    Vec2[] vertices = new Vec2[4];
    vertices[0] = box2d.vectorPixelsToWorld(new Vec2(-500,height/3));
    vertices[1] = box2d.vectorPixelsToWorld(new Vec2(-500,height/2));
    vertices[2] = box2d.vectorPixelsToWorld(new Vec2(500,height/2));
    vertices[3] = box2d.vectorPixelsToWorld(new Vec2(500,height/3));
 
    PolygonShape ps = new PolygonShape();
    ps.set(vertices, vertices.length);

    BodyDef bd = new BodyDef();
    bd.type = BodyType.STATIC;
    bd.position.set(box2d.coordPixelsToWorld(position));
   
    bd.angle = angle; 
    
    body = box2d.createBody(bd);
    
    body.createFixture(ps, 1.0f);
    body.setLinearVelocity(vel);
    body.setAngularVelocity(omega);
  }

  public void display() { 
    Vec2 pos = box2d.getBodyPixelCoord(body);
    float a = body.getAngle();
    Fixture f = body.getFixtureList();
    PolygonShape ps = (PolygonShape) f.getShape();
    
    rectMode(CENTER);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(-a);
    noStroke();
    strokeWeight(2);
    noFill();
    beginShape();
    for (int i = 0; i < ps.getVertexCount(); i++) {
      Vec2 v = box2d.vectorWorldToPixels(ps.getVertex(i));
      vertex(v.x, v.y);
    }
    endShape(CLOSE);
    popMatrix();
  }
}
class Diana { 
  Body body;
  float r = 16;
  
  Diana() {
    makeBody(new Vec2(1200, 105),0,new Vec2(0, 0),0);
    body.setUserData(this);
  } 
  
  public void makeBody(Vec2 position, float angle, Vec2 vel,float omega) {
 
    PolygonShape ps = new PolygonShape();
    float dianaW = box2d.scalarPixelsToWorld(6);
    float dianaH = box2d.scalarPixelsToWorld(50);
     
    ps.setAsBox(dianaW, dianaH);
    
    BodyDef bd = new BodyDef();
    bd.type = BodyType.STATIC;
    bd.position.set(box2d.coordPixelsToWorld(position));
   
    bd.angle = angle; 
    
    body = box2d.createBody(bd);
    
    FixtureDef fd = new FixtureDef();
    fd.shape = ps;
    fd.density = 10;
    fd.friction = 3;

    body.createFixture(fd);
    
    body.setLinearVelocity(vel);
    body.setAngularVelocity(omega);
  }
  
  public void display() { 
    Vec2 pos = box2d.getBodyPixelCoord(body);
    float a = body.getAngle();
    Fixture f = body.getFixtureList();
    PolygonShape ps = (PolygonShape) f.getShape();
    
    rectMode(CENTER);
    pushMatrix();
    translate(pos.x, pos.y);
    rotate(-a);
    noStroke();
    beginShape();
    for (int i = 0; i < ps.getVertexCount(); i++) {
      Vec2 v = box2d.vectorWorldToPixels(ps.getVertex(i));
      vertex(v.x, v.y);
    }
    endShape(CLOSE);
    popMatrix();
  }
}
class Flecha { 
  Body body;
  float r = 147.83f;
  
  Flecha() {
    makeBody(new Vec2(60,190),0,new Vec2(0, 0),0);
    body.setUserData(this);
  } 
  
   public boolean contains(float x, float y) {
    Vec2 worldPoint = box2d.coordPixelsToWorld(x, y);
    Fixture f = body.getFixtureList();
    boolean inside = f.testPoint(worldPoint);
    return inside;
  }
  
  public void makeBody(Vec2 position, float angle, Vec2 vel,float omega) {
     
    PolygonShape ps = new PolygonShape();
    float flechaW = box2d.scalarPixelsToWorld(20);
    float flechaH = box2d.scalarPixelsToWorld(30);
     
    ps.setAsBox(flechaW, flechaH);
    
    BodyDef bd = new BodyDef();
    bd.type = BodyType.DYNAMIC;
    bd.position.set(box2d.coordPixelsToWorld(position));
    
    bd.angle = angle; 
    
    body = box2d.createBody(bd);
    
    FixtureDef fd = new FixtureDef();
    fd.shape = ps;
    fd.density = 2;
    fd.friction = 2;

    body.createFixture(fd);
    body.setLinearVelocity(vel);
    body.setAngularVelocity(omega);
  }
  
  public void angular(float angulo){
      body.setAngularVelocity(angulo);
    }
  
  public void display() { 
    Vec2 pos = box2d.getBodyPixelCoord(body);
    float a = body.getAngle();
    Fixture f = body.getFixtureList();
    PolygonShape ps = (PolygonShape) f.getShape();
 
    pushMatrix();
    image(forma_de_la_flecha, pos.x,pos.y, r, r);
    popMatrix();
  }
}
  public void settings() {  size(1360, 456); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "Tira_al_Blanco" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
