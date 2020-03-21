CREATE TABLE IF NOT EXISTS TB_PROVIDER (
    ID BIGSERIAL PRIMARY KEY NOT NULL,
    NAME CHARACTER VARYING(255) NOT NULL,
    CNPJ CHARACTER VARYING(18) NOT NULL,
    PHONE CHARACTER VARYING(14) NOT NULL,
    CELL_PHONE CHARACTER VARYING(15) NULL,
    PUBLIC_PLACE CHARACTER VARYING(255) NOT NULL,
    NUMBER BIGINT NOT NULL,
    COMPLEMENT CHARACTER VARYING(255),
    NEIGHBORHOOD CHARACTER VARYING(255) NOT NULL,
    CEP CHARACTER VARYING(9) NOT NULL,
    CITY CHARACTER VARYING(50) NOT NULL,
    STATE CHARACTER VARYING(2) NOT NULL
);