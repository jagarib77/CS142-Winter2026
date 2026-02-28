// Air.java

public class Air extends Terrain {
    @Override
    public boolean isCarryable() { return false; }

    @Override
    public boolean isEdible() { return false; }

    @Override
    public char getSymbol() { return ' '; }

    @Override
    public boolean isTraversable() { return false; }

    public boolean isUnmovable() { return true; }

    @Override
    public boolean isSolid() { return false; }
}