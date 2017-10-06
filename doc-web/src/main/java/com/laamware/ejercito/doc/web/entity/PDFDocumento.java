package com.laamware.ejercito.doc.web.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.laamware.ejercito.doc.web.util.GeneralUtils;

@Entity
@Table(name = "DOCUMENTO_PDF")
public class PDFDocumento {

    public static PDFDocumento create() {
        PDFDocumento x = new PDFDocumento();
        x.id = GeneralUtils.newId();
        return x;
    }

    @Id
    @Column(name = "DOCPDF_ID")
    private String id;

    @Column(name = "PDF_TEXTO1")
    private String pdfTexto1;

    @Column(name = "PDF_TEXTO2")
    private String pdfTexto2;

    @Column(name = "PDF_TEXTO3")
    private String pdfTexto3;

    @Column(name = "PDF_TEXTO4")
    private String pdfTexto4;

    @Column(name = "PDF_TEXTO5")
    private String pdfTexto5;

    @Column(name = "PDF_TEXTO6")
    private String pdfTexto6;

    @Column(name = "PDF_TEXTO6_2")
    private String pdfTexto6_2;

    @Column(name = "PDF_TEXTO7")
    private String pdfTexto7;

    @Column(name = "PDF_TEXTO8")
    private String pdfTexto8;

    @Column(name = "PDF_TEXTO9")
    private String pdfTexto9;

    @Column(name = "PDF_TEXTO10")
    private String pdfTexto10;

    @Column(name = "PDF_TEXTO11")
    private String pdfTexto11;

    @Column(name = "PDF_TEXTO12")
    private String pdfTexto12;

    @Column(name = "PDF_TEXTO13")
    private String pdfTexto13;

    @Column(name = "PDF_TEXTO14")
    private String pdfTexto14;

    @Column(name = "PDF_TEXTO15")
    private String pdfTexto15;

    @Column(name = "PDF_TEXTO16")
    private String pdfTexto16;

    @Column(name = "PDF_TEXTO17")
    private String pdfTexto17;

    @Column(name = "PDF_TEXTO18")
    private String pdfTexto18;

    @Column(name = "PDF_TEXTO19")
    private String pdfTexto19;

    @Column(name = "PDF_TEXTO20")
    private String pdfTexto20;

    @Column(name = "PDF_TEXTO21")
    private String pdfTexto21;

    @Column(name = "PDF_TEXTO22")
    private String pdfTexto22;

    @Column(name = "PDF_TEXTO23")
    private String pdfTexto23;

    @Column(name = "PDF_TEXTO24")
    private String pdfTexto24;

    @Column(name = "PDF_TEXTO25")
    private String pdfTexto25;

    @Column(name = "PDF_TEXTO26")
    private String pdfTexto26;

    @Column(name = "PDF_TEXTO27")
    private String pdfTexto27;

    @Column(name = "PDF_TEXTO28")
    private String pdfTexto28;

    @Column(name = "PDF_TEXTO29")
    private String pdfTexto29;

    @Column(name = "PDF_TEXTO30")
    private String pdfTexto30;

    @Column(name = "PDF_TEXTO31")
    private String pdfTexto31;

    @Column(name = "PDF_TEXTO32")
    private String pdfTexto32;

    @Column(name = "PDF_TEXTO33")
    private String pdfTexto33;

    @Column(name = "PDF_TEXTO34")
    private String pdfTexto34;

    @Column(name = "PDF_TEXTO35")
    private String pdfTexto35;

    @Column(name = "PDF_TEXTO36")
    private String pdfTexto36;

    @Column(name = "PDF_TEXTO37")
    private String pdfTexto37;

    @Column(name = "PDF_TEXTO38")
    private String pdfTexto38;

    @Column(name = "PDF_TEXTO39")
    private String pdfTexto39;

    @Column(name = "PDF_TEXTO40")
    private String pdfTexto40;

    @Column(name = "PDF_TEXTO41")
    private String pdfTexto41;

    @Column(name = "PDF_TEXTO42")
    private String pdfTexto42;

    @Column(name = "PDF_TEXTO43")
    private String pdfTexto43;

    @Column(name = "PDF_TEXTO44")
    private String pdfTexto44;

    @Column(name = "PDF_TEXTO45")
    private String pdfTexto45;

    @Column(name = "PDF_TEXTO46")
    private String pdfTexto46;

    @Column(name = "PDF_TEXTO47")
    private String pdfTexto47;

    @Column(name = "PDF_TEXTO48")
    private String pdfTexto48;

    @Column(name = "PDF_TEXTO49")
    private String pdfTexto49;

    @Column(name = "PDF_TEXTO50")
    private String pdfTexto50;

    @Column(name = "PDF_TEXTO51")
    private String pdfTexto51;

    @Column(name = "PDF_TEXTO52")
    private String pdfTexto52;

    @Column(name = "PDF_TEXTO53")
    private String pdfTexto53;

    @Column(name = "PDF_TEXTO54")
    private String pdfTexto54;

    @Column(name = "PDF_TEXTO55")
    private String pdfTexto55;

    @Column(name = "PDF_TEXTO56")
    private String pdfTexto56;

    @Column(name = "PDF_TEXTO57")
    private String pdfTexto57;

    @Column(name = "PDF_TEXTO58")
    private String pdfTexto58;

    @Column(name = "PDF_TEXTO59")
    private String pdfTexto59;

    @Column(name = "PDF_TEXTO60")
    private String pdfTexto60;

    @Column(name = "PDF_TEXTO61")
    private String pdfTexto61;

    @Column(name = "PDF_TEXTO62")
    private String pdfTexto62;

    @Column(name = "PDF_TEXTO63")
    private String pdfTexto63;

    @Column(name = "PDF_TEXTO64")
    private String pdfTexto64;

    @Column(name = "PDF_TEXTO65")
    private String pdfTexto65;

    @Column(name = "PDF_TEXTO66")
    private String pdfTexto66;

    @Column(name = "PDF_TEXTO67")
    private String pdfTexto67;

    @Column(name = "PDF_TEXTO68")
    private String pdfTexto68;

    @Column(name = "PDF_TEXTO69")
    private String pdfTexto69;

    @Column(name = "PDF_TEXTO70")
    private String pdfTexto70;

    @Column(name = "PDF_TEXTO71")
    private String pdfTexto71;

    @Column(name = "PDF_TEXTO72")
    private String pdfTexto72;

    @Column(name = "PDF_TEXTO73")
    private String pdfTexto73;

    @Column(name = "PDF_TEXTO74")
    private String pdfTexto74;

    @Column(name = "PDF_TEXTO75")
    private String pdfTexto75;

    @Column(name = "PDF_TEXTO76")
    private String pdfTexto76;

    @Column(name = "PDF_TEXTO77")
    private String pdfTexto77;

    @Column(name = "PDF_TEXTO78")
    private String pdfTexto78;

    @Column(name = "PDF_TEXTO79")
    private String pdfTexto79;

    @Column(name = "PDF_TEXTO80")
    private String pdfTexto80;

    @Column(name = "PDF_TEXTO81")
    private String pdfTexto81;

    // Issue #123: Nuevo campos PDF_TEXTO para WILDCARDS de generación de
    // documento.
    @Column(name = "PDF_TEXTO82")
    private String pdfTexto82;

    // Issue #123: Nuevo campos PDF_TEXTO para WILDCARDS de generación de
    // documento.
    @Column(name = "PDF_TEXTO83")
    private String pdfTexto83;

    // Issue #129: Nuevo campo PDF_TEXTO para WILDCARDS de generación de
    // documento.
    @Column(name = "PDF_TEXTO84")
    private String pdfTexto84;

    // Issue #129: Nuevo campo PDF_TEXTO para WILDCARDS de generación de
    // documento.
    @Column(name = "PDF_TEXTO85")
    private String pdfTexto85;

    // Issue #129: Nuevo campo PDF_TEXTO para WILDCARDS de generación de
    // documento.
    @Column(name = "PDF_TEXTO86")
    private String pdfTexto86;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPdfTexto1() {
        return pdfTexto1;
    }

    public void setPdfTexto1(String pdfTexto1) {
        this.pdfTexto1 = pdfTexto1;
    }

    public String getPdfTexto2() {
        return pdfTexto2;
    }

    public void setPdfTexto2(String pdfTexto2) {
        this.pdfTexto2 = pdfTexto2;
    }

    public String getPdfTexto3() {
        return pdfTexto3;
    }

    public void setPdfTexto3(String pdfTexto3) {
        this.pdfTexto3 = pdfTexto3;
    }

    public String getPdfTexto4() {
        return pdfTexto4;
    }

    public void setPdfTexto4(String pdfTexto4) {
        this.pdfTexto4 = pdfTexto4;
    }

    public String getPdfTexto5() {
        return pdfTexto5;
    }

    public void setPdfTexto5(String pdfTexto5) {
        this.pdfTexto5 = pdfTexto5;
    }

    public String getPdfTexto6() {
        return pdfTexto6;
    }

    public void setPdfTexto6(String pdfTexto6) {
        this.pdfTexto6 = pdfTexto6;
    }

    public String getPdfTexto6_2() {
        return pdfTexto6_2;
    }

    public void setPdfTexto6_2(String pdfTexto6_2) {
        this.pdfTexto6_2 = pdfTexto6_2;
    }

    public String getPdfTexto7() {
        return pdfTexto7;
    }

    public void setPdfTexto7(String pdfTexto7) {
        this.pdfTexto7 = pdfTexto7;
    }

    public String getPdfTexto8() {
        return pdfTexto8;
    }

    public void setPdfTexto8(String pdfTexto8) {
        this.pdfTexto8 = pdfTexto8;
    }

    public String getPdfTexto9() {
        return pdfTexto9;
    }

    public void setPdfTexto9(String pdfTexto9) {
        this.pdfTexto9 = pdfTexto9;
    }

    public String getPdfTexto10() {
        return pdfTexto10;
    }

    public void setPdfTexto10(String pdfTexto10) {
        this.pdfTexto10 = pdfTexto10;
    }

    public String getPdfTexto11() {
        return pdfTexto11;
    }

    public void setPdfTexto11(String pdfTexto11) {
        this.pdfTexto11 = pdfTexto11;
    }

    public String getPdfTexto12() {
        return pdfTexto12;
    }

    public void setPdfTexto12(String pdfTexto12) {
        this.pdfTexto12 = pdfTexto12;
    }

    public String getPdfTexto13() {
        return pdfTexto13;
    }

    public void setPdfTexto13(String pdfTexto13) {
        this.pdfTexto13 = pdfTexto13;
    }

    public String getPdfTexto14() {
        return pdfTexto14;
    }

    public void setPdfTexto14(String pdfTexto14) {
        this.pdfTexto14 = pdfTexto14;
    }

    public String getPdfTexto15() {
        return pdfTexto15;
    }

    public void setPdfTexto15(String pdfTexto15) {
        this.pdfTexto15 = pdfTexto15;
    }

    public String getPdfTexto16() {
        return pdfTexto16;
    }

    public void setPdfTexto16(String pdfTexto16) {
        this.pdfTexto16 = pdfTexto16;
    }

    public String getPdfTexto17() {
        return pdfTexto17;
    }

    public void setPdfTexto17(String pdfTexto17) {
        this.pdfTexto17 = pdfTexto17;
    }

    public String getPdfTexto18() {
        return pdfTexto18;
    }

    public void setPdfTexto18(String pdfTexto18) {
        this.pdfTexto18 = pdfTexto18;
    }

    public String getPdfTexto19() {
        return pdfTexto19;
    }

    public void setPdfTexto19(String pdfTexto19) {
        this.pdfTexto19 = pdfTexto19;
    }

    public String getPdfTexto20() {
        return pdfTexto20;
    }

    public void setPdfTexto20(String pdfTexto20) {
        this.pdfTexto20 = pdfTexto20;
    }

    public String getPdfTexto21() {
        return pdfTexto21;
    }

    public void setPdfTexto21(String pdfTexto21) {
        this.pdfTexto21 = pdfTexto21;
    }

    public String getPdfTexto22() {
        return pdfTexto22;
    }

    public void setPdfTexto22(String pdfTexto22) {
        this.pdfTexto22 = pdfTexto22;
    }

    public String getPdfTexto23() {
        return pdfTexto23;
    }

    public void setPdfTexto23(String pdfTexto23) {
        this.pdfTexto23 = pdfTexto23;
    }

    public String getPdfTexto24() {
        return pdfTexto24;
    }

    public void setPdfTexto24(String pdfTexto24) {
        this.pdfTexto24 = pdfTexto24;
    }

    public String getPdfTexto25() {
        return pdfTexto25;
    }

    public void setPdfTexto25(String pdfTexto25) {
        this.pdfTexto25 = pdfTexto25;
    }

    public String getPdfTexto26() {
        return pdfTexto26;
    }

    public void setPdfTexto26(String pdfTexto26) {
        this.pdfTexto26 = pdfTexto26;
    }

    public String getPdfTexto27() {
        return pdfTexto27;
    }

    public void setPdfTexto27(String pdfTexto27) {
        this.pdfTexto27 = pdfTexto27;
    }

    public String getPdfTexto28() {
        return pdfTexto28;
    }

    public void setPdfTexto28(String pdfTexto28) {
        this.pdfTexto28 = pdfTexto28;
    }

    public String getPdfTexto29() {
        return pdfTexto29;
    }

    public void setPdfTexto29(String pdfTexto29) {
        this.pdfTexto29 = pdfTexto29;
    }

    public String getPdfTexto30() {
        return pdfTexto30;
    }

    public void setPdfTexto30(String pdfTexto30) {
        this.pdfTexto30 = pdfTexto30;
    }

    public String getPdfTexto31() {
        return pdfTexto31;
    }

    public void setPdfTexto31(String pdfTexto31) {
        this.pdfTexto31 = pdfTexto31;
    }

    public String getPdfTexto32() {
        return pdfTexto32;
    }

    public void setPdfTexto32(String pdfTexto32) {
        this.pdfTexto32 = pdfTexto32;
    }

    public String getPdfTexto33() {
        return pdfTexto33;
    }

    public void setPdfTexto33(String pdfTexto33) {
        this.pdfTexto33 = pdfTexto33;
    }

    public String getPdfTexto34() {
        return pdfTexto34;
    }

    public void setPdfTexto34(String pdfTexto34) {
        this.pdfTexto34 = pdfTexto34;
    }

    public String getPdfTexto35() {
        return pdfTexto35;
    }

    public void setPdfTexto35(String pdfTexto35) {
        this.pdfTexto35 = pdfTexto35;
    }

    public String getPdfTexto36() {
        return pdfTexto36;
    }

    public void setPdfTexto36(String pdfTexto36) {
        this.pdfTexto36 = pdfTexto36;
    }

    public String getPdfTexto37() {
        return pdfTexto37;
    }

    public void setPdfTexto37(String pdfTexto37) {
        this.pdfTexto37 = pdfTexto37;
    }

    public String getPdfTexto38() {
        return pdfTexto38;
    }

    public void setPdfTexto38(String pdfTexto38) {
        this.pdfTexto38 = pdfTexto38;
    }

    public String getPdfTexto39() {
        return pdfTexto39;
    }

    public void setPdfTexto39(String pdfTexto39) {
        this.pdfTexto39 = pdfTexto39;
    }

    public String getPdfTexto40() {
        return pdfTexto40;
    }

    public void setPdfTexto40(String pdfTexto40) {
        this.pdfTexto40 = pdfTexto40;
    }

    public String getPdfTexto41() {
        return pdfTexto41;
    }

    public void setPdfTexto41(String pdfTexto41) {
        this.pdfTexto41 = pdfTexto41;
    }

    public String getPdfTexto42() {
        return pdfTexto42;
    }

    public void setPdfTexto42(String pdfTexto42) {
        this.pdfTexto42 = pdfTexto42;
    }

    public String getPdfTexto43() {
        return pdfTexto43;
    }

    public void setPdfTexto43(String pdfTexto43) {
        this.pdfTexto43 = pdfTexto43;
    }

    public String getPdfTexto44() {
        return pdfTexto44;
    }

    public void setPdfTexto44(String pdfTexto44) {
        this.pdfTexto44 = pdfTexto44;
    }

    public String getPdfTexto45() {
        return pdfTexto45;
    }

    public void setPdfTexto45(String pdfTexto45) {
        this.pdfTexto45 = pdfTexto45;
    }

    public String getPdfTexto46() {
        return pdfTexto46;
    }

    public void setPdfTexto46(String pdfTexto46) {
        this.pdfTexto46 = pdfTexto46;
    }

    public String getPdfTexto47() {
        return pdfTexto47;
    }

    public void setPdfTexto47(String pdfTexto47) {
        this.pdfTexto47 = pdfTexto47;
    }

    public String getPdfTexto48() {
        return pdfTexto48;
    }

    public void setPdfTexto48(String pdfTexto48) {
        this.pdfTexto48 = pdfTexto48;
    }

    public String getPdfTexto49() {
        return pdfTexto49;
    }

    public void setPdfTexto49(String pdfTexto49) {
        this.pdfTexto49 = pdfTexto49;
    }

    public String getPdfTexto50() {
        return pdfTexto50;
    }

    public void setPdfTexto50(String pdfTexto50) {
        this.pdfTexto50 = pdfTexto50;
    }

    public String getPdfTexto52() {
        return pdfTexto52;
    }

    public void setPdfTexto52(String pdfTexto52) {
        this.pdfTexto52 = pdfTexto52;
    }

    public String getPdfTexto53() {
        return pdfTexto53;
    }

    public void setPdfTexto53(String pdfTexto53) {
        this.pdfTexto53 = pdfTexto53;
    }

    public String getPdfTexto54() {
        return pdfTexto54;
    }

    public void setPdfTexto54(String pdfTexto54) {
        this.pdfTexto54 = pdfTexto54;
    }

    public String getPdfTexto51() {
        return pdfTexto51;
    }

    public void setPdfTexto51(String pdfTexto51) {
        this.pdfTexto51 = pdfTexto51;
    }

    public String getPdfTexto55() {
        return pdfTexto55;
    }

    public void setPdfTexto55(String pdfTexto55) {
        this.pdfTexto55 = pdfTexto55;
    }

    public String getPdfTexto56() {
        return pdfTexto56;
    }

    public void setPdfTexto56(String pdfTexto56) {
        this.pdfTexto56 = pdfTexto56;
    }

    public String getPdfTexto57() {
        return pdfTexto57;
    }

    public void setPdfTexto57(String pdfTexto57) {
        this.pdfTexto57 = pdfTexto57;
    }

    public String getPdfTexto58() {
        return pdfTexto58;
    }

    public void setPdfTexto58(String pdfTexto58) {
        this.pdfTexto58 = pdfTexto58;
    }

    public String getPdfTexto59() {
        return pdfTexto59;
    }

    public void setPdfTexto59(String pdfTexto59) {
        this.pdfTexto59 = pdfTexto59;
    }

    public String getPdfTexto60() {
        return pdfTexto60;
    }

    public void setPdfTexto60(String pdfTexto60) {
        this.pdfTexto60 = pdfTexto60;
    }

    public String getPdfTexto61() {
        return pdfTexto61;
    }

    public void setPdfTexto61(String pdfTexto61) {
        this.pdfTexto61 = pdfTexto61;
    }

    public String getPdfTexto62() {
        return pdfTexto62;
    }

    public void setPdfTexto62(String pdfTexto62) {
        this.pdfTexto62 = pdfTexto62;
    }

    public String getPdfTexto63() {
        return pdfTexto63;
    }

    public void setPdfTexto63(String pdfTexto63) {
        this.pdfTexto63 = pdfTexto63;
    }

    public String getPdfTexto64() {
        return pdfTexto64;
    }

    public void setPdfTexto64(String pdfTexto64) {
        this.pdfTexto64 = pdfTexto64;
    }

    public String getPdfTexto65() {
        return pdfTexto65;
    }

    public void setPdfTexto65(String pdfTexto65) {
        this.pdfTexto65 = pdfTexto65;
    }

    public String getPdfTexto66() {
        return pdfTexto66;
    }

    public void setPdfTexto66(String pdfTexto66) {
        this.pdfTexto66 = pdfTexto66;
    }

    public String getPdfTexto67() {
        return pdfTexto67;
    }

    public void setPdfTexto67(String pdfTexto67) {
        this.pdfTexto67 = pdfTexto67;
    }

    public String getPdfTexto68() {
        return pdfTexto68;
    }

    public void setPdfTexto68(String pdfTexto68) {
        this.pdfTexto68 = pdfTexto68;
    }

    public String getPdfTexto69() {
        return pdfTexto69;
    }

    public void setPdfTexto69(String pdfTexto69) {
        this.pdfTexto69 = pdfTexto69;
    }

    public String getPdfTexto70() {
        return pdfTexto70;
    }

    public void setPdfTexto70(String pdfTexto70) {
        this.pdfTexto70 = pdfTexto70;
    }

    public String getPdfTexto71() {
        return pdfTexto71;
    }

    public void setPdfTexto71(String pdfTexto71) {
        this.pdfTexto71 = pdfTexto71;
    }

    public String getPdfTexto72() {
        return pdfTexto72;
    }

    public void setPdfTexto72(String pdfTexto72) {
        this.pdfTexto72 = pdfTexto72;
    }

    public String getPdfTexto73() {
        return pdfTexto73;
    }

    public void setPdfTexto73(String pdfTexto73) {
        this.pdfTexto73 = pdfTexto73;
    }

    public String getPdfTexto74() {
        return pdfTexto74;
    }

    public void setPdfTexto74(String pdfTexto74) {
        this.pdfTexto74 = pdfTexto74;
    }

    public String getPdfTexto75() {
        return pdfTexto75;
    }

    public void setPdfTexto75(String pdfTexto75) {
        this.pdfTexto75 = pdfTexto75;
    }

    public String getPdfTexto76() {
        return pdfTexto76;
    }

    public void setPdfTexto76(String pdfTexto76) {
        this.pdfTexto76 = pdfTexto76;
    }

    public String getPdfTexto77() {
        return pdfTexto77;
    }

    public void setPdfTexto77(String pdfTexto77) {
        this.pdfTexto77 = pdfTexto77;
    }

    public String getPdfTexto78() {
        return pdfTexto78;
    }

    public void setPdfTexto78(String pdfTexto78) {
        this.pdfTexto78 = pdfTexto78;
    }

    public String getPdfTexto79() {
        return pdfTexto79;
    }

    public void setPdfTexto79(String pdfTexto79) {
        this.pdfTexto79 = pdfTexto79;
    }

    public String getPdfTexto80() {
        return pdfTexto80;
    }

    public void setPdfTexto80(String pdfTexto80) {
        this.pdfTexto80 = pdfTexto80;
    }

    public String getPdfTexto81() {
        return pdfTexto81;
    }

    public void setPdfTexto81(String pdfTexto81) {
        this.pdfTexto81 = pdfTexto81;
    }

    /**
     * @return the pdfTexto82
     */
    // Issue #123
    public String getPdfTexto82() {
        return pdfTexto82;
    }

    /**
     * @param pdfTexto82 the pdfTexto82 to set
     */
    // Issue #123
    public void setPdfTexto82(String pdfTexto82) {
        this.pdfTexto82 = pdfTexto82;
    }

    /**
     * @return the pdfTexto83
     */
    // Issue #123
    public String getPdfTexto83() {
        return pdfTexto83;
    }

    /**
     * @param pdfTexto83 the pdfTexto83 to set
     */
    // Issue #123
    public void setPdfTexto83(String pdfTexto83) {
        this.pdfTexto83 = pdfTexto83;
    }

    /**
     * @return the pdfTexto84
     */
    // Issue #129
    public String getPdfTexto84() {
        return pdfTexto84;
    }

    /**
     * @param pdfTexto84 the pdfTexto84 to set
     */
    // Issue #129
    public void setPdfTexto84(String pdfTexto84) {
        this.pdfTexto84 = pdfTexto84;
    }

    /**
     * @return the pdfTexto85
     */
    // Issue #129
    public String getPdfTexto85() {
        return pdfTexto85;
    }

    /**
     * @param pdfTexto85 the pdfTexto85 to set
     */
    // Issue #129
    public void setPdfTexto85(String pdfTexto85) {
        this.pdfTexto85 = pdfTexto85;
    }

    /**
     * @return the pdfTexto86
     */
    // Issue #129
    public String getPdfTexto86() {
        return pdfTexto86;
    }

    /**
     * @param pdfTexto86 the pdfTexto86 to set
     */
    // Issue #129
    public void setPdfTexto86(String pdfTexto86) {
        this.pdfTexto86 = pdfTexto86;
    }
}
