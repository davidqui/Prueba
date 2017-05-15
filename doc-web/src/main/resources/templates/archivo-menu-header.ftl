<#include "archivo-header.ftl">
        <div class="col-md-4 col-lg-3">
          <div class="card">
            <div class="card-header">
              Archivo (Administrables)
            </div>
            <div class="card-block">
              <ul class="nav nav-pills nav-stacked">
                <li class="nav-item">
                    <a href="/admin/edificio" class="nav-link <#if activePill == 'edificio' >active</#if>">Edificios</a>
                </li>
                <li class="nav-item">
                    <a href="/admin/piso" class="nav-link <#if activePill == 'piso' >active</#if>">Pisos</a>
                </li>
                <li class="nav-item">
                    <a href="/admin/area" class="nav-link <#if activePill == 'area' >active</#if>">Áreas</a>
                </li>
                <li class="nav-item">
                    <a href="/admin/modulo" class="nav-link <#if activePill == 'modulo' >active</#if>">Módulos</a>
                </li>
                <li class="nav-item">
                    <a href="/admin/estante" class="nav-link <#if activePill == 'estante' >active</#if>">Estantes</a>
                </li>
                <li class="nav-item">
                    <a href="/admin/entrepano" class="nav-link <#if activePill == 'entrepano' >active</#if>">Entrepaños</a>
                </li>
                <li class="nav-item">
                    <a href="/admin/caja" class="nav-link <#if activePill == 'caja' >active</#if>">Cajas</a>
                </li>
                <li class="nav-item">
                    <a href="/admin/almacenaje" class="nav-link <#if activePill == 'almacenaje' >active</#if>">Almacenaje</a>
                </li>
                <li class="nav-item">
                    <a href="/admin/tipo-caja" class="nav-link <#if activePill == 'tipo-caja' >active</#if>">Tipo de cajas</a>
                </li>
                <li class="nav-item">
                    <a href="/admin/tipo-almacenaje" class="nav-link <#if activePill == 'tipo-almacenaje' >active</#if>">Tipos de almacenaje</a>
                </li>
                               
              </ul>
            </div>
          </div>
        </div>
        <div class="col-md-8 col-lg-9">
