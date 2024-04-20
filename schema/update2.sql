ALTER TABLE "author_h" DROP CONSTRAINT "author_rech_user_fkey";

ALTER TABLE "author" DROP CONSTRAINT "author_record_user_fkey";

ALTER TABLE "book_character" DROP CONSTRAINT "bc_record_user_fkey";

ALTER TABLE "book_character" DROP CONSTRAINT "bc_when_fkey";

ALTER TABLE "book_character_h" DROP CONSTRAINT "bch_record_user_fkey";

ALTER TABLE "book_character_log" DROP CONSTRAINT "bcl_log_book_character_fkey";

ALTER TABLE "book_character_log" DROP CONSTRAINT "bcl_log_chap_fkey";

ALTER TABLE "book_character_log" DROP CONSTRAINT "bcl_record_user_fkey";

ALTER TABLE "book_character_log_h" DROP CONSTRAINT "bclh_record_user_fkey";

ALTER TABLE "book" DROP CONSTRAINT "book_author1_fkey";

ALTER TABLE "book" DROP CONSTRAINT "book_author2_fkey";

ALTER TABLE "book" DROP CONSTRAINT "book_author3_fkey";

ALTER TABLE "book_character" DROP CONSTRAINT "book_character_species_fkey";

ALTER TABLE "book_comment" DROP CONSTRAINT "book_comment_book_fkey";

ALTER TABLE "book_comment" DROP CONSTRAINT "book_comment_user_fkey";

ALTER TABLE "book" DROP CONSTRAINT "book_genre_fkey";

ALTER TABLE "book_rating" DROP CONSTRAINT "book_rating_book_fkey";

ALTER TABLE "book_rating" DROP CONSTRAINT "book_rating_user_fkey";

ALTER TABLE "book" DROP CONSTRAINT "book_record_user_fkey";

ALTER TABLE "book_shelf" DROP CONSTRAINT "book_shelf_book_fkey";

ALTER TABLE "book_shelf" DROP CONSTRAINT "book_shelf_user_fkey";

ALTER TABLE "book_h" DROP CONSTRAINT "bookh_record_user_fkey";

ALTER TABLE "chapter" DROP CONSTRAINT "chapter_book_fkey";

ALTER TABLE "chapter" DROP CONSTRAINT "chapter_record_user_fkey";

ALTER TABLE "chapter_h" DROP CONSTRAINT "chapterh_record_user_fkey";

ALTER TABLE "genre" DROP CONSTRAINT "genre_sub_fkey";

ALTER TABLE "location_log" DROP CONSTRAINT "ll_record_user_fkey";

ALTER TABLE "location_log_h" DROP CONSTRAINT "llh_record_user_fkey";

ALTER TABLE "location" DROP CONSTRAINT "location_location_fkey";

ALTER TABLE "location_log" DROP CONSTRAINT "location_log_chap_fkey";

ALTER TABLE "location_log" DROP CONSTRAINT "location_log_location_fkey";

ALTER TABLE "location" DROP CONSTRAINT "location_record_user_fkey";

ALTER TABLE "location" DROP CONSTRAINT "location_when_fkey";

ALTER TABLE "location_h" DROP CONSTRAINT "locationh_record_user_fkey";

ALTER TABLE "login_history" DROP CONSTRAINT "login_history_user_fkey";

ALTER TABLE "species_log" DROP CONSTRAINT "sl_record_user_fkey";

ALTER TABLE "species_log_h" DROP CONSTRAINT "slh_record_user_fkey";

ALTER TABLE "species_log" DROP CONSTRAINT "species_log_chap_fkey";

ALTER TABLE "species_log" DROP CONSTRAINT "species_log_species_fkey";

ALTER TABLE "species" DROP CONSTRAINT "species_record_user_fkey";

ALTER TABLE "species" DROP CONSTRAINT "species_when_fkey";

ALTER TABLE "species_h" DROP CONSTRAINT "speciesh_record_user_fkey";





--  Remove indexes and checks


--  Add new tables

CREATE TABLE "login_user_h" (
	"history_id" bigint NOT NULL,
	"user_id" bigint NOT NULL,
	"user_password" character(80) NOT NULL,
	"user_fname" character varying(20) NOT NULL,
	"user_lname" character varying(20) NOT NULL,
	"email_address" character varying(80) NOT NULL,
	"birth_date" integer NOT NULL,
	"sex" character(1) DEFAULT 'U' NOT NULL,
	"when_added" timestamp without time zone NOT NULL,
	"when_authorized" timestamp without time zone,
	"administrator" character(1) DEFAULT 'N' NOT NULL,
	"current_uuid" character(36),
	"uuid_last_used" timestamp without time zone,
	"new_email" character varying(80),
	"new_password" character(80),
	"user_status" character(1) DEFAULT 'A' NOT NULL,
	"record_change_date" timestamp without time zone NOT NULL,
	"record_change_type" character(1) NOT NULL,
	"record_user_id" bigint NOT NULL,
	CONSTRAINT "loginh_user_admin_chk" CHECK (((administrator='N')OR(administrator='Y'))),
	CONSTRAINT "loginh_user_status_chk" CHECK (((user_status='A')OR(user_status='D')OR(user_status='B'))),
	CONSTRAINT "loginh_user_sex_chk" CHECK ((((sex='U')OR(sex='M'))OR(sex='F'))),
	CONSTRAINT "loginh_rct_chk" CHECK ((((record_change_type='N')OR(record_change_type='M'))OR(record_change_type='D')))
);


--  Add new columns

ALTER TABLE "login_user" ADD COLUMN "record_change_date" timestamp without time zone NOT NULL DEFAULT '2014-01-01';

ALTER TABLE "login_user" ADD COLUMN "record_change_type" character(1) NOT NULL DEFAULT 'N';

ALTER TABLE "login_user" ADD COLUMN "record_user_id" bigint NOT NULL DEFAULT 1;

ALTER TABLE login_user ALTER COLUMN record_change_date drop default;

ALTER TABLE login_user ALTER COLUMN record_change_type drop default;

ALTER TABLE login_user ALTER COLUMN record_user_id drop default;

ALTER TABLE "login_user" ADD COLUMN "user_status" CHARACTER(1) NOT NULL DEFAULT 'A';
COMMENT ON COLUMN login_user.user_status IS 'Active / Deleted / Banned';


-- New seq

CREATE SEQUENCE login_user_h_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE login_user_h_history_id_seq OWNED BY login_user_h.history_id;

ALTER TABLE ONLY login_user_h ALTER COLUMN history_id SET DEFAULT nextval('login_user_h_history_id_seq'::regclass);


--  Change existing columns


--  Remove tables


--  Drop columns

ALTER TABLE ONLY login_user DROP CONSTRAINT login_user_banned_chk;
ALTER TABLE ONLY login_user DROP COLUMN user_banned;

--  Add new indexes and checks

ALTER TABLE "login_user" ADD CONSTRAINT "login_rct_chk" CHECK ((((record_change_type='N')OR(record_change_type='M'))OR(record_change_type='D')));

ALTER TABLE ONLY "login_user_h" ADD CONSTRAINT "login_user_h_pkey" PRIMARY KEY ("history_id");

CREATE INDEX "login_user_h_record_user_idx" ON "login_user_h" USING btree ("record_user_id");

CREATE INDEX "login_user_record_user_idx" ON "login_user" USING btree ("record_user_id");

ALTER TABLE login_user ADD CONSTRAINT login_user_status_chk CHECK ((((user_status = 'A'::bpchar) OR (user_status = 'D'::bpchar)) OR (user_status = 'B'::bpchar)));



-- ----------------------------------------------------------

ALTER TABLE ONLY "author_h" ADD CONSTRAINT "author_rech_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "author" ADD CONSTRAINT "author_record_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "book_character" ADD CONSTRAINT "bc_record_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "book_character" ADD CONSTRAINT "bc_when_fkey" FOREIGN KEY ("chapter_id") REFERENCES "chapter" ("chapter_id");

ALTER TABLE ONLY "book_character_h" ADD CONSTRAINT "bch_record_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "book_character_log" ADD CONSTRAINT "bcl_log_book_character_fkey" FOREIGN KEY ("book_character_id") REFERENCES "book_character" ("book_character_id");

ALTER TABLE ONLY "book_character_log" ADD CONSTRAINT "bcl_log_chap_fkey" FOREIGN KEY ("chapter_id") REFERENCES "chapter" ("chapter_id");

ALTER TABLE ONLY "book_character_log" ADD CONSTRAINT "bcl_record_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "book_character_log_h" ADD CONSTRAINT "bclh_record_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "book" ADD CONSTRAINT "book_author1_fkey" FOREIGN KEY ("author1_id") REFERENCES "author" ("author_id");

ALTER TABLE ONLY "book" ADD CONSTRAINT "book_author2_fkey" FOREIGN KEY ("author2_id") REFERENCES "author" ("author_id");

ALTER TABLE ONLY "book" ADD CONSTRAINT "book_author3_fkey" FOREIGN KEY ("author3_id") REFERENCES "author" ("author_id");

ALTER TABLE ONLY "book_character" ADD CONSTRAINT "book_character_species_fkey" FOREIGN KEY ("species_id") REFERENCES "species" ("species_id");

ALTER TABLE ONLY "book_comment" ADD CONSTRAINT "book_comment_book_fkey" FOREIGN KEY ("book_id") REFERENCES "book" ("book_id");

ALTER TABLE ONLY "book_comment" ADD CONSTRAINT "book_comment_user_fkey" FOREIGN KEY ("user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "book" ADD CONSTRAINT "book_genre_fkey" FOREIGN KEY ("genre_id") REFERENCES "genre" ("genre_id");

ALTER TABLE ONLY "book_rating" ADD CONSTRAINT "book_rating_book_fkey" FOREIGN KEY ("book_id") REFERENCES "book" ("book_id");

ALTER TABLE ONLY "book_rating" ADD CONSTRAINT "book_rating_user_fkey" FOREIGN KEY ("user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "book" ADD CONSTRAINT "book_record_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "book_shelf" ADD CONSTRAINT "book_shelf_book_fkey" FOREIGN KEY ("book_id") REFERENCES "book" ("book_id");

ALTER TABLE ONLY "book_shelf" ADD CONSTRAINT "book_shelf_user_fkey" FOREIGN KEY ("user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "book_h" ADD CONSTRAINT "bookh_record_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "chapter" ADD CONSTRAINT "chapter_book_fkey" FOREIGN KEY ("book_id") REFERENCES "book" ("book_id");

ALTER TABLE ONLY "chapter" ADD CONSTRAINT "chapter_record_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "chapter_h" ADD CONSTRAINT "chapterh_record_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "genre" ADD CONSTRAINT "genre_sub_fkey" FOREIGN KEY ("sub_genre_id") REFERENCES "genre" ("genre_id");

ALTER TABLE ONLY "location_log" ADD CONSTRAINT "ll_record_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "location_log_h" ADD CONSTRAINT "llh_record_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "location" ADD CONSTRAINT "location_location_fkey" FOREIGN KEY ("inside_location_id") REFERENCES "location" ("location_id");

ALTER TABLE ONLY "location_log" ADD CONSTRAINT "location_log_chap_fkey" FOREIGN KEY ("chapter_id") REFERENCES "chapter" ("chapter_id");

ALTER TABLE ONLY "location_log" ADD CONSTRAINT "location_log_location_fkey" FOREIGN KEY ("location_id") REFERENCES "location" ("location_id");

ALTER TABLE ONLY "location" ADD CONSTRAINT "location_record_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "location" ADD CONSTRAINT "location_when_fkey" FOREIGN KEY ("chapter_id") REFERENCES "chapter" ("chapter_id");

ALTER TABLE ONLY "location_h" ADD CONSTRAINT "locationh_record_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "login_history" ADD CONSTRAINT "login_history_user_fkey" FOREIGN KEY ("user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "login_user" ADD CONSTRAINT "login_user_rec_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "login_user_h" ADD CONSTRAINT "login_user_rech_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "species_log" ADD CONSTRAINT "sl_record_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "species_log_h" ADD CONSTRAINT "slh_record_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "species_log" ADD CONSTRAINT "species_log_chap_fkey" FOREIGN KEY ("chapter_id") REFERENCES "chapter" ("chapter_id");

ALTER TABLE ONLY "species_log" ADD CONSTRAINT "species_log_species_fkey" FOREIGN KEY ("species_id") REFERENCES "species" ("species_id");

ALTER TABLE ONLY "species" ADD CONSTRAINT "species_record_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

ALTER TABLE ONLY "species" ADD CONSTRAINT "species_when_fkey" FOREIGN KEY ("chapter_id") REFERENCES "chapter" ("chapter_id");

ALTER TABLE ONLY "species_h" ADD CONSTRAINT "speciesh_record_user_fkey" FOREIGN KEY ("record_user_id") REFERENCES "login_user" ("user_id");

