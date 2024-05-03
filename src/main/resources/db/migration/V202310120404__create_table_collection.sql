CREATE TABLE COLLECTION (
	ID BIGINT NOT NULL UNIQUE,
    NAME VARCHAR(255) NOT NULL,
    CUSTOMER BIGINT NOT NULL,
    CREATED_DATE TIMESTAMP WITH TIME ZONE,
    LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE COLLECTION_SEQ START WITH 1;

ALTER TABLE COLLECTION ADD PRIMARY KEY (ID);
ALTER TABLE COLLECTION ADD CONSTRAINT COLLECTION_CUSTOMER_ID_FK FOREIGN KEY (CUSTOMER) REFERENCES CUSTOMER(ID);