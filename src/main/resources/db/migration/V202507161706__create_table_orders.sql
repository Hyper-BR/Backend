CREATE TABLE ORDERS (
    ID UUID PRIMARY KEY,
    CUSTOMER_ID UUID         NOT NULL,
    STATUS VARCHAR(20)       NOT NULL DEFAULT 'PENDING',
    TOTAL_AMOUNT DECIMAL(10,2) NOT NULL,
    CREATED_DATE TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    LAST_MODIFIED_DATE TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE ORDERS ADD CONSTRAINT ORDERS_CUSTOMER_ID_FK FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMERS(ID) ON DELETE RESTRICT;