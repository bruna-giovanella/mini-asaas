<!doctype html>
<html lang="pt-BR">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="Grails"/>
    </title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>
    <link
        rel="stylesheet"
        href="https://atlas.asaas.com/v15.18.0/atlas.min.css"
        crossorigin="anonymous">
    <script
        defer
        src="https://atlas.asaas.com/v15.18.0/atlas.min.js"
        crossorigin="anonymous"
    ></script>
    <asset:javascript src="script.js"/>
    <g:layoutHead/>
    <style>
        body, html {
            height: 100%;
        }

        .floating-alert {
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 9999;
            width: auto;
            max-width: 400px;
        }
    </style>
</head>
<body>
    <atlas-screen>
        <atlas-navbar slot="navbar">
            <atlas-icon-button
                slot="actions"
                icon="bell"
                size="4x"
                hoverable
                tooltip="Notificações"
            >
            </atlas-icon-button>
        </atlas-navbar>

        <atlas-sidebar slot="sidebar" product="asaas" home-path="/customer/index">
            <atlas-sidebar-menu slot="body">
                <atlas-sidebar-menu-item
                    value="customer"
                    icon="home"
                    text="Dashboard"
                    href="${createLink(controller: 'customer', action: 'index', id: customer?.id)}">
                    active
                >
                </atlas-sidebar-menu-item>

                <atlas-sidebar-menu-item
                    value="users"
                    icon="users"
                    text="Usuários"
                    href="/user/index"
                >
                </atlas-sidebar-menu-item>

                <atlas-sidebar-menu-item
                    value="payer"
                    icon="address-book"
                    text="Clientes"
                    href="/payer/index"
                >
                </atlas-sidebar-menu-item>

                <atlas-sidebar-menu-item
                    value="payment"
                    icon="wallet"
                    text="Cobranças"
                    href="/payment/index"
                >
                </atlas-sidebar-menu-item>

                <atlas-sidebar-menu-item
                    value="logout"
                    icon="x"
                    text="Logout"
                    href="${createLink(controller: 'login', action: 'logout')}">
                >
                </atlas-sidebar-menu-item>

            </atlas-sidebar-menu>
        </atlas-sidebar>

        <atlas-page class="js-atlas-page">
            <atlas-page-content slot="content" class="js-atlas-content">
                <g:layoutBody />
            </atlas-page-content>
        </atlas-page>
    </atlas-screen>
</body>
</html>