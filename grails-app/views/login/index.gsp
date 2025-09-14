<g:applyLayout name="external">

    <g:if test="${flash.message}">
        <div class="floating-alert">
            <atlas-alert type="danger">
                ${flash.message}
            </atlas-alert>
        </div>
    </g:if>

    <div class="split-container">
        <div class="left-side">
            <atlas-panel class="login-panel">

                <atlas-text size="lg" bold>Login</atlas-text>

                <g:form controller="login" action="auth" method="post">
                    <atlas-grid>
                        <atlas-row>
                            <atlas-col>
                                <atlas-text size="sm" bold>Usuário</atlas-text>
                                <g:textField name="username" id="username" required="true"/>
                            </atlas-col>
                        </atlas-row>

                        <atlas-row>
                            <atlas-col>
                                <atlas-text size="sm" bold>Senha</atlas-text>
                                <g:passwordField name="password" id="password" required="true"/>
                            </atlas-col>
                        </atlas-row>
                    </atlas-grid>

                    <atlas-divider></atlas-divider>

                    <atlas-layout gap="2" inline>
                        <button
                            type="submit"
                            class="btn-primario">
                            Entrar
                        </button>

                        <atlas-button
                            type="outlined"
                            theme="primary"
                            description="Criar uma conta"
                            href="${createLink(controller: 'customer', action: 'create')}">
                            Criar uma conta
                        </atlas-button>
                    </atlas-layout>
                </g:form>

            </atlas-panel>
        </div>

        <div class="right-side"></div>
    </div>

</g:applyLayout>

<style>
    html, body {
        margin: 0;
        padding: 0;
        height: 100%;
    }

    .js-atlas-content {
        padding: 0 !important;
    }

    .js-atlas-page {
        display: flex;
        height: 100vh;
    }

    .split-container {
        display: flex;
        height: 100vh;
        width: 100vw;
    }

    .left-side {
        flex: 0.75;
        display: flex;
        justify-content: center;
        align-items: center;
        background-color: #f8f9fa;
    }

    .login-panel {
        width: 500px;
        max-width: 100%;
        padding: 20px;
        box-sizing: border-box;
    }

    .right-side {
        flex: 1;
        background-image: url('${resource(dir: 'images', file: 'login-mini-asaas.png')}');
        background-size: cover;
        background-position: center;
    }


    .btn-primario {
        background-color: #002cbd;
        color: white;
        border: none;
        border-radius: 4px;
        padding: 8px 16px;
        font-size: 16px;
        font-weight: 540
        cursor: pointer;
        transition: background-color 0.2s ease;
    }

    .btn-primario:hover {
        background-color: #00167a;
    }
</style>