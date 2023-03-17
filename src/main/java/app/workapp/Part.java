package app.workapp;

public class Part {
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPartNum() {
        return partNum;
    }

    public void setPartNum(String partNum) {
        this.partNum = partNum;
    }

    String product, partName, description, partNum;

    public Part(String product, String partNum, String partName, String description){
        this.product = product;
        this.partName = partName;
        this.partNum = partNum;
        this.description = description;
    }

    public String toString(){
        String result = "";

        result = "\t" + product + "\t\t" + partName + "\t\t" + partNum + "\t\t" + description;

        return result;
    }
}
