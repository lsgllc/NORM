package com.txdot.isd.rts.services.util;// cmp.TRT.TRT.java Translate ASCII <=> EBCDIC or ASCII <=> IBMOEM/*TRT class to translate to ASCII <=> EBCDIC or ASCII <=>IBMOEM. It only translates a single character. You will haveto call its methods repeatedly to translate strings, bytearrays or files. Warning! There are many national variantsof EBCDIC. Seehttp://www.software.ibm.com/ts/mqseries/support/faqs/convert.html#HDREBVARIf you want one of the national variants, you will have toadjust the EBCDIC descriptor table in TRT.TrtBuild.Version 1.1 adds support of IBMOEMtranslations.*//** * @author  copyright (c) 1999-2002 Roedy Green, Canadian Mind Products * may be copied and used freely for any purpose but military. * * Roedy Green * Canadian Mind Products * #327 - 964 Heywood Avenue * Victoria, BC Canada V8V 2Y5 * tel: (250) 361-9093 * mailto:roedy@mindprod.com * http://mindprod.com * * version 1.1 1999 January 11 *             - translate ASCII soft hyphen to EBCDIC - *             - additional tables for ASCII <=> IBMOEM * version 1.0 1999 January 11 *             - initial release *//*futures:- load trt from resource as needed instead of building it in with static init.- Amanuensis that will translate the clipboard as requested.*//* &TRT& */public class TRT {/* &TRT'debugging& */   private static boolean debugging = true;/* &TRT'EmbeddedCopyright& */   private static final String EmbeddedCopyright =   "copyright (c) 1999-2002 Roedy Green, Canadian Mind Products, http://mindprod.com";   /**    * ASCII to EBCDIC translate table.    * source code generated by cmp.TRT.TrtBuild    *//* &TRT'AToE& */   private static byte[] AToE = {      /* 0 */0, 1, 2, 3, 55, 45, 46, 47, 22, 5, 21, 11, 12, 13, 14, 15,      /* 16 */  16, 17, 18, 63, 60, 61, 50, 38, 24, 25, 63, 39, 28, 29, 30, 31,      /* 32 */  64, 90, 127, 123, 91, 108, 80, 125, 77, 93, 92, 78, 107, 96, 75, 97,      /* 48 */  -16, -15, -14, -13, -12, -11, -10, -9, -8, -7, 122, 94, 76, 126, 110, 111,      /* 64 */  124, -63, -62, -61, -60, -59, -58, -57, -56, -55, -47, -46, -45, -44, -43, -42,      /* 80 */  -41, -40, -39, -30, -29, -28, -27, -26, -25, -24, -23, 63, 63, 63, 63, 109,      /* 96 */  -71, -127, -126, -125, -124, -123, -122, -121, -120, -119, -111, -110, -109, -108, -107, -106,      /* 112 */  -105, -104, -103, -94, -93, -92, -91, -90, -89, -88, -87, 63, 79, 63, 63, 7,      /* 128 */  63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,      /* 144 */  63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,      /* 160 */  64, 63, 74, 123, 63, 63, 63, 63, 63, 63, 63, 63, 95, 96, 63, 63,      /* 176 */  63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,      /* 192 */  63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,      /* 208 */  63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,      /* 224 */  63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,      /* 240 */  63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63   };   /**     * EBCDIC to ASCII translate table.     * source code generated by cmp.TRT.TrtBuild     *//* &TRT'EToA& */   private static byte[] EToA = {      /* 0 */0, 1, 2, 3, 26, 9, 26, 127, 26, 26, 26, 11, 12, 13, 14, 15,      /* 16 */  16, 17, 18, 26, 26, 10, 8, 26, 24, 25, 26, 26, 28, 29, 30, 31,      /* 32 */  26, 26, 28, 26, 26, 10, 23, 27, 26, 26, 26, 26, 26, 5, 6, 7,      /* 48 */  26, 26, 22, 26, 26, 30, 26, 4, 26, 26, 26, 26, 20, 21, 26, 26,      /* 64 */  32, 26, 26, 26, 26, 26, 26, 26, 26, 26, -94, 46, 60, 40, 43, 124,      /* 80 */  38, 26, 26, 26, 26, 26, 26, 26, 26, 26, 33, 36, 42, 41, 59, -84,      /* 96 */  45, 47, 26, 26, 26, 26, 26, 26, 26, 26, 26, 44, 37, 95, 62, 63,      /* 112 */  26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 58, 35, 64, 39, 61, 34,      /* 128 */  26, 97, 98, 99, 100, 101, 102, 103, 104, 105, 26, 26, 26, 26, 26, 26,      /* 144 */  26, 106, 107, 108, 109, 110, 111, 112, 113, 114, 26, 26, 26, 26, 26, 26,      /* 160 */  26, 26, 115, 116, 117, 118, 119, 120, 121, 122, 26, 26, 26, 26, 26, 26,      /* 176 */  26, 26, 26, 26, 26, 26, 26, 26, 26, 96, 26, 26, 26, 26, 26, 26,      /* 192 */  26, 65, 66, 67, 68, 69, 70, 71, 72, 73, 26, 26, 26, 26, 26, 26,      /* 208 */  26, 74, 75, 76, 77, 78, 79, 80, 81, 82, 26, 26, 26, 26, 26, 26,      /* 224 */  26, 26, 83, 84, 85, 86, 87, 88, 89, 90, 26, 26, 26, 26, 26, 26,      /* 240 */  48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 26, 26, 26, 26, 26, 26   };   /**     * ASCII to IBMOEM translate table.     * source code generated by cmp.TRT.TrtBuild     *//* &TRT'AToI& */   private static byte[] AToI = {      /* 0 */0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,      /* 16 */  16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,      /* 32 */  32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47,      /* 48 */  48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63,      /* 64 */  64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79,      /* 80 */  80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95,      /* 96 */  96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111,      /* 112 */  112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127,      /* 128 */  26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26,      /* 144 */  26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 26,      /* 160 */  -1, -83, -101, -100, 26, -99, 26, 26, 26, 26, -90, -82, -86, 26, 26, 26,      /* 176 */  26, -15, -3, 26, 26, -26, 26, -7, 26, 26, -89, -81, -84, -85, 26, -88,      /* 192 */  26, 26, 26, 26, -114, -113, -110, -128, 26, -112, 26, 26, 26, 26, 26, 26,      /* 208 */  26, -91, 26, 26, 26, 26, -103, 26, 26, 26, 26, 26, -102, 26, 26, 26,      /* 224 */  -123, -96, -125, 26, -124, -122, -111, -121, -118, -126, -120, -119, -115, -95, -116, -117,      /* 240 */  26, -92, -107, -94, -109, 26, -108, -10, 26, -105, -93, -106, -127, 26, 26, -104   };   /**     * IBMOEM to ASCII translate table.     * source code generated by cmp.TRT.TrtBuild     *//* &TRT'IToA& */   private static byte[] IToA = {      /* 0 */0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,      /* 16 */  16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,      /* 32 */  32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47,      /* 48 */  48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63,      /* 64 */  64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79,      /* 80 */  80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95,      /* 96 */  96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111,      /* 112 */  112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127,      /* 128 */  -57, -4, -23, -30, -28, -32, -27, -25, -22, -21, -24, -17, -18, -20, -60, -59,      /* 144 */  -55, -26, -58, -12, -10, -14, -5, -7, -1, -42, -36, -94, -93, -91, 26, 26,      /* 160 */  -31, -19, -13, -6, -15, -47, -86, -70, -65, 26, -84, -67, -68, -95, -85, -69,      /* 176 */  42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42,      /* 192 */  42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42,      /* 208 */  42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42, 42,      /* 224 */  26, 26, 26, 26, 26, 26, -75, 26, 26, 26, 26, 26, 26, 26, 26, 26,      /* 240 */  26, -79, 26, 26, 26, 26, -9, 26, 26, -73, 26, 26, 26, -78, 26, -96   };   /**    * translate a single byte 0 .. 255 from ASCII (Latin-1) to EBCDIC.    * Also works with signed bytes -128 .. 127.    *//* &TRT.ASCIIToEBCDIC& */   public static int ASCIIToEBCDIC (int ascii ) {      // 0xff ugliness required because Java bytes are signed.      return AToE[ascii & 0xff] & 0xff;   } // end ASCIIToEBCDIC   /**     * translate a single byte 0 .. 255 from ASCII (Latin-1) to IBMOEM.     * Also works with signed bytes -128 .. 127.     *//* &TRT.ASCIIToIBMOEM& */   public static int ASCIIToIBMOEM (int ascii ) {      // 0xff ugliness required because Java bytes are signed.      return AToI[ascii & 0xff] & 0xff;   } // end ASCIIToIBMOEM   /**    * translate a single byte 0 .. 255 from  EBCDIC to ASCII (Latin-1).    * Also works with signed bytes -128 .. 127.    *//* &TRT.EBCDICToASCII& */   public static int EBCDICToASCII (int ebcdic ) {      // 0xff ugliness required because Java bytes are signed.      return EToA[ebcdic & 0xff] & 0xff;   } // end EBCDICToASCII   /**    * translate a single byte 0 .. 255 from  IBMOEM to ASCII (Latin-1).    * Also works with signed bytes -128 .. 127.    *//* &TRT.IBMOEMToASCII& */   public static int IBMOEMToASCII (int ibmoem ) {      // 0xff ugliness required because Java bytes are signed.      return IToA[ibmoem & 0xff] & 0xff;   } // end IBMOEMToASCII   /**       * small test driver       *//* &TRT.main& */   public static void main (String[] args ) {      if ( debugging ) {         if ( TRT.ASCIIToEBCDIC('9') == 249              && TRT.EBCDICToASCII(249) == '9'              && TRT.ASCIIToIBMOEM(160) == 255              && TRT.IBMOEMToASCII(148) == 246 ) {            System.out.println("translate works");         }      }   } // end main}/* #TRT# */ // end class TRT