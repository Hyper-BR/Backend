CREATE TABLE SUBSCRIPTIONS (
    ID SERIAL PRIMARY KEY,
    TYPE VARCHAR(20) UNIQUE NOT NULL,
    NAME VARCHAR(50) NOT NULL,
    DESCRIPTION TEXT,
    MONTHLY_PRICE NUMERIC(38,2),
    FREE_TRACK_LIMIT INTEGER,
    CREATED_DATE TIMESTAMP,
    LAST_MODIFIED_DATE TIMESTAMP
);

INSERT INTO SUBSCRIPTIONS(TYPE, NAME, DESCRIPTION, MONTHLY_PRICE, CREATED_DATE, LAST_MODIFIED_DATE) VALUES
('FREE_LISTENER', 'Gratuito Ouvinte', 'Ouça músicas gratuitamente com anúncios. Não permite publicação de faixas.', 0.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('FREE_ARTIST', 'Gratuito Artista', 'Publique até 5 faixas. Ouça com anúncios. Recursos limitados para criadores iniciantes.', 0.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SOLO', 'Plano Solo', 'Tenha acesso total à música sem anúncios. Ouça com qualidade máxima e sem interrupções. Não permite publicação de faixas.', 4.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ARTIST', 'Plano Artista', 'Publique faixas ilimitadas. Acesso a dashboards interativos e ferramentas de criação. Música sem anúncios.', 9.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('LABEL', 'Plano Gravadora', 'Gerencie múltiplos artistas, publique faixas ilimitadas e acesse dashboards avançados. Ferramentas de promoção e suporte profissional incluídos.', 19.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
