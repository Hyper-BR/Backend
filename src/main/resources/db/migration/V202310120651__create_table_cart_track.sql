CREATE TABLE CART_TRACK (
	CUSTOMER_ID VARCHAR(20) NOT NULL,
    TRACK_ID BIGINT NOT NULL,
    CREATED_DATE TIMESTAMP,
    LAST_MODIFIED_DATE TIMESTAMP
);

--ALTER TABLE CART_TRACK ADD CONSTRAINT CART_TRACK_CUSTOMER_ID_FK FOREIGN KEY (CUSTOMER_ID) REFERENCES CART(CUSTOMER_ID);
ALTER TABLE CART_TRACK ADD CONSTRAINT CART_TRACK_TRACK_ID_FK FOREIGN KEY (TRACK_ID) REFERENCES TRACK(ID);