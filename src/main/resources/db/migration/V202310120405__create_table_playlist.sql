CREATE TABLE PLAYLIST (
	ID BIGINT NOT NULL UNIQUE,
    NAME VARCHAR(255) NOT NULL,
    DESCRIPTION VARCHAR(255),
    CUSTOMER BIGINT NOT NULL,
    COLLECTION BIGINT NOT NULL,
    CREATED_DATE TIMESTAMP WITH TIME ZONE,
    LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
);

CREATE SEQUENCE PLAYLIST_SEQ START WITH 1;

ALTER TABLE PLAYLIST ADD PRIMARY KEY (ID);
ALTER TABLE PLAYLIST ADD CONSTRAINT PLAYLIST_CUSTOMER_ID_FK FOREIGN KEY (CUSTOMER) REFERENCES CUSTOMER(ID);
ALTER TABLE PLAYLIST ADD CONSTRAINT PLAYLIST_COLLECTION_ID_FK FOREIGN KEY (COLLECTION) REFERENCES COLLECTION(ID);