package LinkedInteger;

public class LinkedBits implements Comparable
{
    // holds dummy link which sits in front of the most significant bit
    private BitLink head;
    // holds dummy link which sits behind the least significant bit
    private BitLink tail;
    private int size;

    /*
     * O(1)
     * @return a LinkedBits object representing zero
     */
    public LinkedBits()
    {
        head = new BitLink(false);
        size = 0;
        tail = new BitLink(false);

        head.lessSig = tail;
        tail.moreSig = head;
    }

    /*
     * O(n)
     * copy constructor
     */
    public LinkedBits(LinkedBits lb)
    {
        head = new BitLink(false);
        size = 0;
        tail = new BitLink(false);

        head.lessSig = tail;
        tail.moreSig = head;

        BitLink curBit = lb.tail.moreSig;
        while(curBit.moreSig != null)
        {
            this.pushMostSig(curBit.value);
            curBit = curBit.moreSig;
        }
    }

    /*
     * O(1)
     * @return the size of the list in bits (disregarding head and tail)
     */
    public int getSize()
    {
        return size;
    }

    /*
     * O(size-trueSize)
     * @return the size of the LinkedBits minus leading zeros
     */
    public int getTrueSize()
    {
       if(size == 0) return 0;
       int trueSize = size;
       BitLink curBit = head.lessSig;
       while(!curBit.value && curBit.lessSig != null)
       {
            trueSize--;
            curBit = curBit.lessSig;
       }
       return trueSize;
    }

    /*
     * O(size)
     * @param bit - the bit to look for (least significant=0, most significant=size-1)
     * @return the boolean value at the specified index
     */
    public boolean getBit(int bit)
    {
        BitLink theBit = null;
        if(bit > (size-1)/2)
        {
            theBit = head;
            for(int i = size; i > bit; i--)
            {
                theBit = theBit.lessSig;
                if(theBit == null) return false;
            }
        }
        else
        {
            theBit = tail;
            for(int i = -1; i < bit; i++)
            {
                theBit = theBit.moreSig;
                if(theBit == null) return false;
            }
        }


        return theBit.value;
    }

    /*
     * O(size)
     * @return number in binary form as a String
     */
    public String toString()
    {
        if(size == 0)
        {
            return "0";
        }
        String number = "";
        BitLink c = head;
        for(int i = 0; i < size; i++)
        {
            c = c.lessSig;
            if(c.value)
            {
                number += "1";
            }
            else
            {
                number += "0";
            }
        }
        return number;
    }


    /*
     * NOT YET IMPLEMENTED
     * @return number in decimal form as a String
     */
    public String toDecimalString()
    {
        return "";
    }

    /*
     * O(max(size,a.size))
     * @param a - the number to add
     * @return the sum as a new LinkedBits Object
     */
    public LinkedBits add(LinkedBits a)
    {
        LinkedBits sum = new LinkedBits();
        
        BitLink term1 = this.tail.moreSig;
        BitLink term2 = a.tail.moreSig;

        int current = 0;

        while(term1.moreSig != null || term2.moreSig != null || current > 0)
        {
            if(term1.value) current++;
            if(term2.value) current++;
            if(current == 1 || current == 3)
            {
                sum.pushMostSig(true);
            }
            else
            {
                sum.pushMostSig(false);
            }
            if(current == 1 || current == 0)
            {
                current = 0;
            }
            else
            {
                current = 1;
            }
            if(term1.moreSig != null) term1 = term1.moreSig;
            if(term2.moreSig != null) term2 = term2.moreSig;
        }

        sum.cutDown();

        return sum;
    }

    /*
     * O(max(size,a.size))
     * @param a - the number to subtract
     * @return the difference as a new LinkedBits Object
     * note - LinkedBits are unsigned so the absolute value is returned
     * => a.difference(b) is the same as b.difference(a)
     */
    public LinkedBits difference(LinkedBits a)
    {
        LinkedBits dif = new LinkedBits();

        BitLink term1 = this.tail.moreSig;
        BitLink term2 = a.tail.moreSig;
        // want to subtract the lesser from the greater
        if(this.compareTo(a) < 0)
        {
            term1 = a.tail.moreSig;
            term2 = this.tail.moreSig;
        }

        int current = 0;

        while(term1.moreSig != null || term2.moreSig != null || current != 0)
        {
            if(term1.value) current++;
            if(term2.value) current--;

            if(current == 1)
            {
                dif.pushMostSig(true);
                current = 0;
            }
            else if(current == 0)
            {
                dif.pushMostSig(false);
            }
            else if(current == -1)
            {
                dif.pushMostSig(true);
            }
            else if(current == -2)
            {
                dif.pushMostSig(false);
                current = -1;
            }

            if(term1.moreSig != null) term1 = term1.moreSig;
            if(term2.moreSig != null) term2 = term2.moreSig;
        }

        dif.cutDown();

        return dif;
    }

    /*
     * O(size^2)
     * @param a - the number to multiply by
     * @return the product as a new LinkedBits Object
     */
    public LinkedBits multiply(LinkedBits a)
    {
        LinkedBits product = new LinkedBits();
        LinkedBits multiplicand = new LinkedBits(this);
        multiplicand.cutDown();
        BitLink curBit = a.tail.moreSig;

        while(curBit.moreSig != null)
        {
            if(curBit.value)
            {
                product = product.add(multiplicand);
            }
            multiplicand.pushLeastSig(false);
            curBit = curBit.moreSig;
        }

        product.cutDown();

        return product;
    }

    /*
     * NOT YET IMPLEMENTED
     * @param a - the number to divide by
     * @return the quotient rounded down (integer division)
     */
    public LinkedBits divide(LinkedBits a)
    {
        LinkedBits thisCopy = new LinkedBits(this);
        LinkedBits quotient = new LinkedBits();

        thisCopy.cutDown();
        a.cutDown();
        int trailingZeros = thisCopy.cutUp();

        

        while(trailingZeros>0)
        {
            quotient.pushLeastSig(false);
            trailingZeros--;
        }
        quotient.cutDown();
        
        return quotient;
    }

    /*
     * NOT IMPLEMENTED
     * @param a - the number to divide by
     * @return the remainder after dividing by a
     */
    public LinkedBits mod(LinkedBits a)
    {
        return new LinkedBits();
    }

    /*
     * O(1)
     * pushes a BitLink onto the front of the LinkedBit
     * @param value - the boolean value of the BitLink to be pushed
     */
    public void pushMostSig(boolean value)
    {
        BitLink newBit = new BitLink(value);
        newBit.lessSig = head.lessSig;
        newBit.moreSig = head;
        head.lessSig.moreSig = newBit;
        head.lessSig = newBit;
        size++;
    }

    /*
     * O(1)
     * pushes a BitLink onto the back of the LinkedBit
     * @param value - the boolean value of the BitLink to be pushed
     */
    public void pushLeastSig(boolean value)
    {
        BitLink newBit = new BitLink(value);
        newBit.moreSig = tail.moreSig;
        newBit.lessSig = tail;
        tail.moreSig.lessSig = newBit;
        tail.moreSig = newBit;
        size++;
    }

    /*
     * O(1)
     * removes the most significant bit
     * @return the value of the most significant bit
     */
    public boolean popMostSig()
    {
        if(size > 0)
        {
            boolean val = head.lessSig.value;
            head.lessSig.lessSig.moreSig = head;
            head.lessSig = head.lessSig.lessSig;
            size--;
            return val;
        }
        return false;
    }

    /*
     * O(1)
     * removes the least significant bit
     * @return the value of the least significant bit
     */
    public boolean popLeastSig()
    {
        if(size > 0)
        {
            boolean val = tail.moreSig.value;
            tail.moreSig.moreSig.lessSig = tail;
            tail.moreSig = tail.moreSig.moreSig;
            size--;
            return val;
        }
        return false;
    }
    
    /*
     * O(size)
     * @param o - the object to be compared
     * @return 1 if this is greater than o, -1 if o is greater, 0 if equal
     */
    public int compareTo(Object o)
    {
        if(!(o instanceof LinkedBits)) return 0;
        LinkedBits a = (LinkedBits)o;

        int size1 = this.size;
        int size2 = a.size;

        BitLink term1 = this.head.lessSig;
        BitLink term2 = a.head.lessSig;

        while(size1>size2)
        {
            if(term1.value) return 1;
            size1--;
            term1 = term1.lessSig;
        }
        while(size2>size1)
        {
            if(term2.value) return -1;
            size2--;
            term2 = term2.lessSig;
        }

        while(term1.lessSig != null && term2.lessSig != null)
        {
            if(term1.value && !term2.value) return 1;
            if(term2.value && !term1.value) return -1;
            term1 = term1.lessSig;
            term2 = term2.lessSig;
        }

        return 0;
    }

    /*
     * O(size)
     * @param o - the object to be compared
     * @return whether the object is a LinkedBits object with
     * the same numerical value
     */
    public boolean equals(Object o)
    {
        if(!(o instanceof LinkedBits)) return false;
        LinkedBits a = (LinkedBits)o;
        return (this.compareTo(a)==0);
    }

    /*
     * O(size-trueSize)
     * removes leading zeros from LinkedBits
     * @return the number of bits taken off
     */
    public int cutDown()
    {
        int total = 0;
        while(size>0 && !head.lessSig.value)
        {
            this.popMostSig();
            total++;
        }
        return total;
    }

    /*
     * O(size)
     * removes trailing zeros from LinkedBits
     * @return the number of bits taken off
     */
    public int cutUp()
    {
        int total = 0;
        while(size>0 && !tail.moreSig.value)
        {
            this.popLeastSig();
            total++;
        }
        return total;
    }
    /*
     * O(size)
     * tests if the numerical value is zero
     * @return whether or not the value is zero
     */
    public boolean isZero()
    {
        BitLink curBit = head.lessSig;
        while(curBit != null)
        {
            if(curBit.value) return false;
            curBit = curBit.lessSig;
        }
        return true;
    }
    
}
