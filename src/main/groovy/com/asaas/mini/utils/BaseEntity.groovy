package com.asaas.mini.utils

class BaseEntity {
    Date dateCreated;
    Date lastUpdate;
    Boolean deleted = false;

    static mapping = {
        tablePerHierarchy false
    }

    void softDelete() {
        this.deleted = true;
    }

    void restore() {
        this.deleted = false;
    }

}

/*
MAPPING

    1. o nome mapping é predefinido pelo GORM. O Grails (via GORM) procura um campo mapping dentro das classes de
domínio para entender como mapear a classe no banco de dados.

    2. O Grails só reconhece *static mapping* como bloco de configuração do mapeamento ORM


TABLE PER HIERARCHY - TPH

    1. Quando é usado herança, o hibernate decide como irá criar tabelas no banco de dados;

    2. Table per hierarchy (TPH) é ativado por padrão, com ele, todas as entidades filhas da BaseEntity ficam na mesma
tabela, sendo diferenciadas por uma coluna Class, que diferencia os tipos;

    3. Ao definir o TPH como falso, é definido que é desejado uma tabela para cada entidade;

    4. Com a desativação do TPH é ativado o TPS = cada entidade filha tem sua própria tabela no banco. É incluido
automaticamente em cada tabela as colunass herdadas de BaseEntity.
*/
