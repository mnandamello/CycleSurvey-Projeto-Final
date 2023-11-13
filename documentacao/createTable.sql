CREATE SEQUENCE seq_acessorio
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 1000
    NOCYCLE;

CREATE SEQUENCE seq_info_bike
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 1000
    NOCYCLE;

CREATE SEQUENCE seq_pessoa_fisica
    START WITH 1
    INCREMENT BY 1
    MAXVALUE 1000
    NOCYCLE;

-- 2. Criando a tabela para armazenar dados da pessoa física
CREATE TABLE tb_pessoa_fisica
(
    id_pf          NUMBER(10) PRIMARY KEY,
    nm_completo    VARCHAR2(40)  NOT NULL,
    dt_nascimento  DATE          NOT NULL,
    cpf            VARCHAR2(14)  NOT NULL,
    celular        VARCHAR2(14)  NOT NULL,
    cep            VARCHAR2(9)   NOT NULL,
    cidade         VARCHAR2(50)  NOT NULL,
    logradouro     VARCHAR2(100) NOT NULL,
    num_logradouro VARCHAR2(8)   NOT NULL,
    estado         CHAR(2)       NOT NULL,
    complemento    VARCHAR2(20)
);

-- 3. Criando a tabela para armazenar dados da bicicleta
CREATE TABLE tb_info_bike
(
    id_bike     NUMBER(10) PRIMARY KEY,
    marca       VARCHAR2(25) NOT NULL,
    modelo      VARCHAR2(30) NOT NULL,
    valor       VARCHAR2(11) NOT NULL,
    ano_compra  VARCHAR2(4)  NOT NULL,
    nota_fiscal VARCHAR2(40) NOT NULL,
    id_pf       NUMBER(10)   NOT NULL,
    CONSTRAINT fk_pessoa_fisica FOREIGN KEY (id_pf) REFERENCES tb_pessoa_fisica (id_pf)
);

-- 4. Criando a tabela para armazenar dados do acessório da bicicleta
CREATE TABLE tb_acessorio
(
    id_acessorio          NUMBER(10) PRIMARY KEY,
    nota_fiscal_acessorio VARCHAR2(40) NOT NULL,
    marca_acessorio       VARCHAR2(25) NOT NULL,
    modelo                VARCHAR2(40) NOT NULL,
    tipo_acessorio        VARCHAR2(30) NOT NULL,
    valor                 NUMBER(19,2) NOT NULL,
    id_bike               NUMBER(10),
    CONSTRAINT fk_info_bike FOREIGN KEY (id_bike) REFERENCES tb_info_bike (id_bike)
);

-- 5. Inserindo dados na tabela Pessoa Física

INSERT INTO tb_pessoa_fisica (id_pf, nm_completo, dt_nascimento, cpf, celular, cep, cidade, logradouro, num_logradouro,
                              estado, complemento)
VALUES (seq_pessoa_fisica.NEXTVAL, 'Renan dos Santos', TO_DATE('01-01-2001', 'DD-MM-YYYY'), '47209828487',
        '11987480978', '09201922', 'São Paulo', 'Rua das jabutiqueiras', '132', 'SP', 'Setor B');

INSERT INTO tb_pessoa_fisica (id_pf, nm_completo, dt_nascimento, cpf, celular, cep, cidade, logradouro, num_logradouro,
                              estado, complemento)
VALUES (seq_pessoa_fisica.NEXTVAL, 'Daiane dos Santos', TO_DATE('24-07-2005', 'DD-MM-YYYY'), '27200828487',
        '11934480978', '09201923', 'Santo André', 'Rua das salsichas', '123', 'SP', 'Casa de cima');

INSERT INTO tb_pessoa_fisica (id_pf, nm_completo, dt_nascimento, cpf, celular, cep, cidade, logradouro, num_logradouro,
                              estado, complemento)
VALUES (seq_pessoa_fisica.NEXTVAL, 'Paulo Roberto Gomes', TO_DATE('12-09-1998', 'DD-MM-YYYY'), '47209899987',
        '11911480978', '09201924', 'São Paulo', 'Rua Conceição', '1', 'MG', 'B');

INSERT INTO tb_pessoa_fisica (id_pf, nm_completo, dt_nascimento, cpf, celular, cep, cidade, logradouro, num_logradouro,
                              estado, complemento)
VALUES (seq_pessoa_fisica.NEXTVAL, 'Maria Silva', TO_DATE('21-02-1988', 'DD-MM-YYYY'), '47243428487', '11900480978',
        '09201925', 'São Caetano', 'Rua Sarambé', '978', 'SP', '23B');

INSERT INTO tb_pessoa_fisica (id_pf, nm_completo, dt_nascimento, cpf, celular, cep, cidade, logradouro, num_logradouro,
                              estado, complemento)
VALUES (seq_pessoa_fisica.NEXTVAL, 'Nicolas Gabriel', TO_DATE('27-08-2005', 'DD-MM-YYYY'), '11109828487', '11978481278',
        '19201926', 'Juazeiro', 'Rua das petunias', '222', 'PA', '3C');

INSERT INTO tb_pessoa_fisica (id_pf, nm_completo, dt_nascimento, cpf, celular, cep, cidade, logradouro, num_logradouro,
                              estado, complemento)
VALUES (seq_pessoa_fisica.NEXTVAL, 'Gabrielle Rodrigues', TO_DATE('04-05-2005', 'DD-MM-YYYY'), '25209828487',
        '1194448-0978', '29201927', 'São Paulo', 'Rua dos anjos', '221', 'SP', '13A');

INSERT INTO tb_pessoa_fisica (id_pf, nm_completo, dt_nascimento, cpf, celular, cep, cidade, logradouro, num_logradouro,
                              estado, complemento)
VALUES (seq_pessoa_fisica.NEXTVAL, 'Lucca Robersval', TO_DATE('03-05-2001', 'DD-MM-YYYY'), '99203828487', '11999480978',
        '09201892', 'São Paulo', 'Rua do professor me da 10', '007', 'SP', '3B');


-- 6. Inserindo dados na tabela Info_Bike
INSERT INTO tb_info_bike (id_bike, marca, modelo, valor, ano_compra, nota_fiscal, id_pf)
VALUES (seq_info_bike.NEXTVAL, 'Caloi', 'Super V8', '4000', '2020', '0090049012000234000041234000365709190000', 1);

INSERT INTO tb_info_bike (id_bike, marca, modelo, valor, ano_compra, nota_fiscal, id_pf)
VALUES (seq_info_bike.NEXTVAL, 'Viking', 'MTB Series', '2000', '2018', '1293923812000234000042224000365709190000', 2);

INSERT INTO tb_info_bike (id_bike, marca, modelo, valor, ano_compra, nota_fiscal, id_pf)
VALUES (seq_info_bike.NEXTVAL, 'Caloi', 'Super V8', '4000', '2020', '2133123412000234000011124000365709190000', 3);

INSERT INTO tb_info_bike (id_bike, marca, modelo, valor, ano_compra, nota_fiscal, id_pf)
VALUES (seq_info_bike.NEXTVAL, 'Viking', 'Hill Series', '10000', '2017', '154204901200023453225502400036570910000', 4);

INSERT INTO tb_info_bike (id_bike, marca, modelo, valor, ano_compra, nota_fiscal, id_pf)
VALUES (seq_info_bike.NEXTVAL, 'Caloi', 'Super V8', '4000', '2020', '7452325112000234000034234000365709190000', 5);

INSERT INTO tb_info_bike (id_bike, marca, modelo, valor, ano_compra, nota_fiscal, id_pf)
VALUES (seq_info_bike.NEXTVAL, 'Oggi', 'Panamera', '3500', '2015', '4576765412000234000043234000365709190000', 6);

INSERT INTO tb_info_bike (id_bike, marca, modelo, valor, ano_compra, nota_fiscal, id_pf)
VALUES (seq_info_bike.NEXTVAL, 'Caloi', 'Super V8', '4000', '2020', '7456045712000234000055024000365709190000', 7);


-- 5. Inserindo dados na tabela Acessório
INSERT INTO tb_acessorio (id_acessorio, nota_fiscal_acessorio, marca_acessorio, modelo, tipo_acessorio, valor, id_bike)
VALUES (seq_acessorio.NEXTVAL, '12344321123443211234', 'Vivant', 'CT 49', 'Velocímetro', '500', 1);

INSERT INTO tb_acessorio (id_acessorio, nota_fiscal_acessorio, marca_acessorio, modelo, tipo_acessorio, valor, id_bike)
VALUES (seq_acessorio.NEXTVAL, '23454321123443211234', 'Caloi', 'PO 15', 'Manômetro', '800', 2);

INSERT INTO tb_acessorio (id_acessorio, nota_fiscal_acessorio, marca_acessorio, modelo, tipo_acessorio, valor, id_bike)
VALUES (seq_acessorio.NEXTVAL, '34564321123443211234', 'Vivant', 'CT 49', 'Velocímetro', '1234', 3);

INSERT INTO tb_acessorio (id_acessorio, nota_fiscal_acessorio, marca_acessorio, modelo, tipo_acessorio, valor, id_bike)
VALUES (seq_acessorio.NEXTVAL, '45674321123443211234', 'Vivant', 'Air Hawk', 'Suspensão a Ar', '5000', 4);

INSERT INTO tb_acessorio (id_acessorio, nota_fiscal_acessorio, marca_acessorio, modelo, tipo_acessorio, valor, id_bike)
VALUES (seq_acessorio.NEXTVAL, '56784321123443211234', 'Vikingx', 'VK2', 'Velocímetro', '1000', 5);

INSERT INTO tb_acessorio (id_acessorio, nota_fiscal_acessorio, marca_acessorio, modelo, tipo_acessorio, valor, id_bike)
VALUES (seq_acessorio.NEXTVAL, '67894321123443211234', 'Oggi', 'Cloud 1', 'Suspensão Hidráulica', '10000', 6);

INSERT INTO tb_acessorio (id_acessorio, nota_fiscal_acessorio, marca_acessorio, modelo, tipo_acessorio, valor, id_bike)
VALUES (seq_acessorio.NEXTVAL, '78914321123443211234', 'Vivant', 'CT 49', 'Velocímetro', '1000', 7);


COMMIT ;