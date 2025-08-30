<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Logout</title>
</head>
<body>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h3 class="text-center">Confirmar Logout</h3>
                    </div>
                    <div class="card-body">
                        <p class="text-center">
                            Tem certeza que deseja sair do sistema?
                        </p>
                        <div class="text-center">
                            <g:link controller="logout" action="logout" class="btn btn-danger">Sim, Sair</g:link>
                            <g:link controller="customer" action="index" class="btn btn-secondary">Cancelar</g:link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
