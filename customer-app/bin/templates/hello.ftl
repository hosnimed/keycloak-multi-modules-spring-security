<#import "/spring.ftl" as spring />
<#assign xhtmlCompliant = true in spring>
<!DOCTYPE html>
<html>
<head>
    <title>${serviceName}</title>
</head>
<body>

<header>
    <a href="${productServiceUrl}">Products</a>
    <a href="${customerServiceUrl}">Customers</a>
    <form action="<@spring.url '/sso/logout' />" method="post">
        <input type="hidden"
               name="${_csrf.parameterName}"
               value="${_csrf.token}"/>
        <input type="submit" name="submit" value="Logout"/>
    </form>

</header>

<h2>Greetings List</h2>
<ul>
    <#if  greetings??>
    <#list greetings as greeting>
        <li>ID :: ${greeting.id} - CONTENT :: ${greeting.content}</li>
    </#list>
    <#else>
        <li> No Customers List found !</li>
    </#if>
</ul>

</body>
</html>
