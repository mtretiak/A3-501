
import java.io.Serializable;

/**
 * Created by mtretiak on 2017-11-12.
 */
public class DummbyObject implements Serializable{
        private String firstName;
        private String lastName;
        private String middleName;
        private boolean bool;
        private byte myByte;
        private char myChar;
        private short myShort;
        private int 	myInt;
        private long myLong;
        private float myFloat;
        private double myDouble;

        public char[] str = {'h','e','l','l','o'};


        public DummbyObject(){
            this.firstName = "Michael";
            this.lastName = "Tretiak";
            this.middleName = "John";
            this.bool = false;
            this.myByte = 0;
            this.myChar = 'O';
            this.myShort = 5555;
            this.myInt = 20;
            this.myLong = 55555;
            this.myFloat = 3.6f;
            this.myDouble = 5.5;

        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public byte getMyByte() {
            return myByte;
        }

        public void setMyByte(byte myByte) {
            this.myByte = myByte;
        }

        public boolean isBool() {
            return bool;
        }

        public void setBool(boolean bool) {
            this.bool = bool;
        }

        public char getMyChar() {
            return myChar;
        }

        public void setMyChar(char myChar) {
            this.myChar = myChar;
        }

        public short getMyShort() {
            return myShort;
        }

        public void setMyShort(short myShort) {
            this.myShort = myShort;
        }

        public int getMyInt() {
            return myInt;
        }

        public void setMyInt(int myInt) {
            this.myInt = myInt;
        }

        public long getMyLong() {
            return myLong;
        }

        public void setMyLong(long myLong) {
            this.myLong = myLong;
        }

        public float getMyFloat() {
            return myFloat;
        }

        public void setMyFloat(float myFloat) {
            this.myFloat = myFloat;
        }

        public double getMyDouble() {
            return myDouble;
        }

        public void setMyDouble(double myDouble) {
            this.myDouble = myDouble;
        }



    }
