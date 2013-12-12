

package LinkedInteger;

class BitLink
{
    boolean value;
    BitLink moreSig;
    BitLink lessSig;

    public BitLink(boolean val)
    {
        value = val;
        moreSig = null;
        lessSig = null;
    }
}
