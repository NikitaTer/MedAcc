package by.nikiter.model.entity;

public class PackagingUnit {

    private String name;
    private double productQuantity;
    private Unit unit;
    private int quantityInBox;

    private double addExpPiece;
    private double addExpSet;

    private double pieceCost;
    private double setCost;

    private double piecePrice;
    private double setPrice;

    public PackagingUnit(String name, double productQuantity, Unit unit, double addExpPiece, int quantityInBox, double addExpSet) {
        this.name = name;
        this.productQuantity = productQuantity;
        this.unit = unit;
        this.addExpPiece = addExpPiece;
        this.quantityInBox = quantityInBox;
        this.addExpSet = addExpSet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(double productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public double getPiecePrice() {
        return piecePrice;
    }

    public void setPiecePrice(double piecePrice) {
        this.piecePrice = piecePrice;
    }

    public double getAddExpPiece() {
        return addExpPiece;
    }

    public void setAddExpPiece(double addExpPiece) {
        this.addExpPiece = addExpPiece;
    }

    public int getQuantityInBox() {
        return quantityInBox;
    }

    public void setQuantityInBox(int quantityInBox) {
        this.quantityInBox = quantityInBox;
    }

    public double getAddExpSet() {
        return addExpSet;
    }

    public void setAddExpSet(double addExpSet) {
        this.addExpSet = addExpSet;
    }

    public double getSetPrice() {
        return setPrice;
    }

    public void setSetPrice(double setPrice) {
        this.setPrice = setPrice;
    }

    public double getPieceCost() {
        return pieceCost;
    }

    public void setPieceCost(double pieceCost) {
        this.pieceCost = pieceCost;
    }

    public double getSetCost() {
        return setCost;
    }

    public void setSetCost(double setCost) {
        this.setCost = setCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PackagingUnit that = (PackagingUnit) o;

        if (Double.compare(that.productQuantity, productQuantity) != 0) return false;
        if (quantityInBox != that.quantityInBox) return false;
        if (Double.compare(that.addExpPiece, addExpPiece) != 0) return false;
        if (Double.compare(that.addExpSet, addExpSet) != 0) return false;
        if (Double.compare(that.pieceCost, pieceCost) != 0) return false;
        if (Double.compare(that.setCost, setCost) != 0) return false;
        if (Double.compare(that.piecePrice, piecePrice) != 0) return false;
        if (Double.compare(that.setPrice, setPrice) != 0) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return unit == that.unit;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        temp = Double.doubleToLongBits(productQuantity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + quantityInBox;
        temp = Double.doubleToLongBits(addExpPiece);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(addExpSet);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(pieceCost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(setCost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(piecePrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(setPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "PackagingUnit{" +
                "name='" + name + '\'' +
                ", productQuantity=" + productQuantity +
                ", unit=" + unit +
                ", quantityInBox=" + quantityInBox +
                ", addExpPiece=" + addExpPiece +
                ", addExpSet=" + addExpSet +
                ", piecePrice=" + piecePrice +
                ", boxPrice=" + setPrice +
                ", pieceCost=" + pieceCost +
                ", boxCost=" + setCost +
                '}';
    }
}
