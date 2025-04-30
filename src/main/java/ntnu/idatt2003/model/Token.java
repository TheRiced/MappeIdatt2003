package ntnu.idatt2003.model;



public class Token {
  private final int id;
  private final LudoPlayer owner;
  private int position = -1;

  public Token(int id, LudoPlayer owner) {
    this.id = id;
    this.owner = owner;
  }

  public int getId() { return id; }
  public LudoPlayer getOwner() { return owner; }
  public int getPosition() { return position; }
  public void setPosition(int position) { this.position = position; }

  public boolean isAtHome() { return position < 0; }

  public boolean isFinished() {
    return position == owner.getFinishEntry() + Board.FINISH_STEPS;
  }

}
