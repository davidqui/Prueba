package com.laamware.ejercito.doc.web.docx4j;

import java.util.List;

public class TablaDTO
{
  private List<String> listaCabeceraTabla;
  private List<List<String>> listaFilasTabla;
  
  public TablaDTO(List<String> listaCabeceraTabla, List<List<String>> listaFilasTabla)
  {
    this.listaCabeceraTabla = listaCabeceraTabla;
    this.listaFilasTabla = listaFilasTabla;
  }
  
  public List<String> getListaCabeceraTabla()
  {
    return this.listaCabeceraTabla;
  }
  
  public void setListaCabeceraTabla(List<String> listaCabeceraTabla)
  {
    this.listaCabeceraTabla = listaCabeceraTabla;
  }
  
  public List<List<String>> getListaFilasTabla()
  {
    return this.listaFilasTabla;
  }
  
  public void setListaFilasTabla(List<List<String>> listaFilasTabla)
  {
    this.listaFilasTabla = listaFilasTabla;
  }
}

