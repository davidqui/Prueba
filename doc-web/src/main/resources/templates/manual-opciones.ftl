<#include "header.ftl">
        <div class="col-md-4 col-lg-3">
          <div class="card">
            <div class="card-header">
              Temario Manual de Usuario
            </div>
            <div class="card-block">
              <ul class="nav nav-pills nav-stacked">
              	<#if tematicas?? >
                <#list tematicas as tematica>
                <li class="nav-item">
                  <a href="/manual/multimedia/${tematica.id}" class="nav-link" >${tematica.nombre?capitalize}</a>
                </li>
                </#list>
				</#if>
              </ul>
            </div>
          </div>
        </div>
        <div class="col-md-8 col-lg-9">