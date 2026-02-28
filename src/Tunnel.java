// Tunnel.java

public class Tunnel extends Terrain {
    @Override
    public boolean isCarryable() { return false; }

    @Override
    public boolean isEdible() { return false; }

    @Override
    public char getSymbol() { return '.'; }

    @Override
    public boolean isTraversable() { return true; }

    @Override
    public boolean isSolid() { return false; }
}