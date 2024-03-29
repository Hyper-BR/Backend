CREATE TABLE CUSTOMER (
	ID BIGINT NOT NULL UNIQUE,
    CUSTOMER_ID VARCHAR(20) NOT NULL UNIQUE,
    NAME VARCHAR(50) NOT NULL,
    PASSWORD VARCHAR(50) NOT NULL,
    EMAIL VARCHAR(50) NOT NULL UNIQUE,
    USERNAME VARCHAR(30) NOT NULL UNIQUE,
    COUNTRY VARCHAR(20) NOT NULL,
    BIRTH_DATE DATE NOT NULL,
    AVATAR VARCHAR(255),
    SUBSCRIPTION_ID INT NOT NULL DEFAULT 1,
    DESCRIPTION VARCHAR(255),
    CREATED_DATE TIMESTAMP,
    LAST_MODIFIED_DATE TIMESTAMP
);

CREATE SEQUENCE CUSTOMER_SEQ START WITH 1;

ALTER TABLE CUSTOMER ADD PRIMARY KEY (CUSTOMER_ID);
ALTER TABLE CUSTOMER ADD CONSTRAINT SUBSCRIPTION_ID_FK FOREIGN KEY (SUBSCRIPTION_ID) REFERENCES SUBSCRIPTION(ID);