/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
