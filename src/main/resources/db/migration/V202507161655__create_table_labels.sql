CREATE TABLE LABELS (
	ID UUID PRIMARY KEY,
    CUSTOMER_ID UUID NOT NULL,
    USERNAME VARCHAR(255) NOT NULL UNIQUE,
    IS_VERIFIED BOOLEAN DEFAULT FALSE,
    CREATED_DATE TIMESTAMP WITH TIME ZONE,
    LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE
);

ALTER TABLE LABELS ADD CONSTRAINT LABELS_CUSTOMER_ID_FK FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMERS(ID) ON DELETE RESTRICT;