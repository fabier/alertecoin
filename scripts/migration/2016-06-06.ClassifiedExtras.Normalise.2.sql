ALTER TABLE classified_extra
  ADD CONSTRAINT fk_classified_extra_classified
    FOREIGN KEY (classified_id) REFERENCES classified (id)
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE classified_classified_extra
  DROP CONSTRAINT fk_ots1fhr47jibc5dfvxdgtw2aq;

ALTER TABLE classified_extra_old
  DROP CONSTRAINT classified_extra_pkey;

ALTER TABLE classified_extra
  ADD CONSTRAINT classified_extra_pkey PRIMARY KEY(classified_id, key_id);


-- ALTER TABLE classified_extra
--   DROP COLUMN id;

-- DROP TABLE classified_extra_old;
-- DROP TABLE classified_classified_extra;