class Flecha { 
  Body body;
  float r = 147.83;
  
  Flecha() {
    makeBody(new Vec2(60,190),0,new Vec2(0, 0),0);
    body.setUserData(this);
  } 
  
   boolean contains(float x, float y) {
    Vec2 worldPoint = box2d.coordPixelsToWorld(x, y);
    Fixture f = body.getFixtureList();
    boolean inside = f.testPoint(worldPoint);
    return inside;
  }
  
  void makeBody(Vec2 position, float angle, Vec2 vel,float omega) {
     
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
  
  void angular(float angulo){
      body.setAngularVelocity(angulo);
    }
  
  void display() { 
    Vec2 pos = box2d.getBodyPixelCoord(body);
    float a = body.getAngle();
    Fixture f = body.getFixtureList();
    PolygonShape ps = (PolygonShape) f.getShape();
 
    pushMatrix();
    image(forma_de_la_flecha, pos.x,pos.y, r, r);
    popMatrix();
  }
}
