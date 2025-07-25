CREATE TABLE TRACK_TAGS (
    TRACK_ID UUID NOT NULL,
    TAG_ID   BIGINT NOT NULL,
    PRIMARY KEY (TRACK_ID, TAG_ID)
);

ALTER TABLE TRACK_TAGS ADD CONSTRAINT TRACK_TAGS_TRACK_ID_FK FOREIGN KEY (TRACK_ID) REFERENCES TRACKS(ID) ON DELETE RESTRICT;
ALTER TABLE TRACK_TAGS ADD CONSTRAINT TRACK_TAGS_TAG_ID_FK FOREIGN KEY (TAG_ID) REFERENCES TAGS(ID) ON DELETE RESTRICT;