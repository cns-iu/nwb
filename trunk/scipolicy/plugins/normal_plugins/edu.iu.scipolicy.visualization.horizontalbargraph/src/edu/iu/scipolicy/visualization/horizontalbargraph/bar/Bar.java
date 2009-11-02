package edu.iu.scipolicy.visualization.horizontalbargraph.bar;

public class Bar {
       private String label;
       private boolean continuesLeft;
       private boolean continuesRight;
       private double x;
       private double width;
       private double height;
       private double originalAmount;

       public Bar(
                       String label,
                       boolean continuesLeft,
                       boolean continuesRight,
                       double x,
                       double width,
                       double height,
                       double originalAmount) {
               this.label = label;
               this.continuesLeft = continuesLeft;
               this.continuesRight = continuesRight;
               this.x = x;
               this.width = width;
               this.height = height;
               this.originalAmount = originalAmount;
       }

       public String getLabel() {
               return this.label;
       }

       public boolean continuesLeft() {
               return this.continuesLeft;
       }

       public boolean continuesRight() {
               return this.continuesRight;
       }

       public double getX() {
               return this.x;
       }

       public double getWidth() {
               return this.width;
       }

       public double getHeight() {
               return this.height;
       }

       public double getOriginalAmount() {
               return this.originalAmount;
       }
}