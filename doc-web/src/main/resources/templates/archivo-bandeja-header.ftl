<#include "util-macros.ftl" />
<#include "archivo-header.ftl">
        <div class="col-md-4 col-lg-3">
          <div class="card">
            <div class="card-header">
              Bandejas
            </div>
            <div class="card-block">
              <ul class="nav nav-pills nav-stacked">
                <#list bandejas as b>
                <li class="nav-item">
                  <a href="/bandeja/${b.id}" class="nav-link <#if b.id == bandeja.id>active</#if>">${b.nombre}</a>
                </li>
                </#list>
              </ul>
            </div>
          </div>
        </div>
        <div class="col-md-8 col-lg-9">
          <h1 class="cus-h1-page-title">${pageTitle}</h1>
          <@flash/>