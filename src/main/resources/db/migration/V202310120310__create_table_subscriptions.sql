CREATE TABLE SUBSCRIPTIONS (
    ID BIGSERIAL PRIMARY KEY,
    TYPE VARCHAR(20) UNIQUE NOT NULL,
    NAME VARCHAR(50) NOT NULL,
    DESCRIPTION TEXT,
    PAYMENT_ID VARCHAR(50) NOT NULL,
    PRICE NUMERIC(38,2),
    CREATED_DATE TIMESTAMP,
    LAST_MODIFIED_DATE TIMESTAMP
);

INSERT INTO SUBSCRIPTIONS(TYPE, NAME, DESCRIPTION, PAYMENT_ID, PRICE, CREATED_DATE, LAST_MODIFIED_DATE) VALUES
('FREE', 'Plano Gratuito', 'Ouça músicas gratuitamente. Não permite publicação de faixas.', '',0.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('PREMIUM', 'Plano Premium', 'Tenha acesso total à música sem anúncios. Ouça com qualidade máxima e sem interrupções. Não permite publicação de faixas.', 'price_1RtWT6Qs8Aq2jGSv7HSTKJoH', 4.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ARTIST', 'Plano Artista', 'Publique faixas ilimitadas. Acesso a dashboards interativos e ferramentas de criação. Música sem anúncios.', 'price_1RtWT6Qs8Aq2jGSvJJxFj8W8', 9.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('LABEL', 'Plano Gravadora', 'Gerencie múltiplos artistas, publique faixas ilimitadas e acesse dashboards avançados. Ferramentas de promoção e suporte profissional incluídos.', 'price_1RtWT6Qs8Aq2jGSv9aRJkhMr', 19.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
