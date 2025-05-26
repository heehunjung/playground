ALTER TABLE article
    ADD COLUMN member_id BIGINT;

ALTER TABLE article
    ADD CONSTRAINT FK_article_member
        FOREIGN KEY (member_id)
            REFERENCES member(id);
