<#include "header.ftl">
        <div class="col-md-4 col-lg-3">
          <div class="card">
            <div class="card-header">
              <a href="/manual/intro" class="nav-link" ><img src="/img/menu.svg" style="margin-right:2px;"><b>Temario Manual de Usuario</b></a>
            </div>
            <div class="card-block">
              <ul class="nav nav-pills nav-stacked">
              	<#if tematicas?? >
                <#list tematicas as tematica>
                <li class="nav-item">
                  <a href="/manual/multimedia/${tematica.id}" class="nav-link" ><img src="/img/book-open.svg" style="height: 14px; margin-right:2px;"> ${tematica.nombre?capitalize}</a>
                </li>
                </#list>
				</#if>
              </ul>
            </div>
          </div>
        </div>
        <div class="col-md-8 col-lg-9">