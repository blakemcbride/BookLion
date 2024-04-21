--
-- PostgreSQL database dump
--

-- Dumped from database version 12.18 (Ubuntu 12.18-0ubuntu0.20.04.1)
-- Dumped by pg_dump version 12.18 (Ubuntu 12.18-0ubuntu0.20.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'SQL_ASCII';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: author; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.author (
    author_id bigint NOT NULL,
    lname character varying(40) NOT NULL,
    fname character varying(40),
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    CONSTRAINT author_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.author OWNER TO postgres;

--
-- Name: author_author_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.author_author_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.author_author_id_seq OWNER TO postgres;

--
-- Name: author_author_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.author_author_id_seq OWNED BY public.author.author_id;


--
-- Name: author_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.author_h (
    history_id bigint NOT NULL,
    author_id bigint NOT NULL,
    lname character varying(40) NOT NULL,
    fname character varying(40),
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    CONSTRAINT author_rcth_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.author_h OWNER TO postgres;

--
-- Name: author_h_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.author_h_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.author_h_history_id_seq OWNER TO postgres;

--
-- Name: author_h_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.author_h_history_id_seq OWNED BY public.author_h.history_id;


--
-- Name: book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book (
    book_id bigint NOT NULL,
    genre_id integer NOT NULL,
    author1_id bigint NOT NULL,
    author2_id bigint,
    author3_id bigint,
    book_title character varying(80) NOT NULL,
    year_published integer DEFAULT 0 NOT NULL,
    description character varying(2000),
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    CONSTRAINT book_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.book OWNER TO postgres;

--
-- Name: book_book_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_book_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.book_book_id_seq OWNER TO postgres;

--
-- Name: book_book_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.book_book_id_seq OWNED BY public.book.book_id;


--
-- Name: book_character; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_character (
    book_character_id bigint NOT NULL,
    chapter_id bigint NOT NULL,
    species_id bigint NOT NULL,
    lname character varying(60),
    fname character varying(50),
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    suffix character varying(5),
    nickname character varying(20),
    gender character(1) DEFAULT 'U'::bpchar NOT NULL,
    relevance character(1) DEFAULT 'U'::bpchar NOT NULL,
    affiliation character varying(30),
    occupation character varying(30),
    relationship character varying(30),
    CONSTRAINT bc_gender_chk CHECK (((gender = 'U'::bpchar) OR (gender = 'M'::bpchar) OR (gender = 'F'::bpchar) OR (gender = 'N'::bpchar))),
    CONSTRAINT bc_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar))),
    CONSTRAINT bc_relevance_chk CHECK (((relevance = 'P'::bpchar) OR (relevance = 'S'::bpchar) OR (relevance = 'I'::bpchar) OR (relevance = 'U'::bpchar)))
);


ALTER TABLE public.book_character OWNER TO postgres;

--
-- Name: COLUMN book_character.chapter_id; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.book_character.chapter_id IS 'The chapter the character was introduced in';


--
-- Name: COLUMN book_character.gender; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.book_character.gender IS '(U)nspecified
Male
Female
None';


--
-- Name: COLUMN book_character.relevance; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.book_character.relevance IS 'Primary
Secondary
Incidental
Unspecified';


--
-- Name: COLUMN book_character.relationship; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.book_character.relationship IS 'Relationship to other characters';


--
-- Name: CONSTRAINT bc_gender_chk ON book_character; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON CONSTRAINT bc_gender_chk ON public.book_character IS '
';


--
-- Name: book_character_book_character_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_character_book_character_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.book_character_book_character_id_seq OWNER TO postgres;

--
-- Name: book_character_book_character_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.book_character_book_character_id_seq OWNED BY public.book_character.book_character_id;


--
-- Name: book_character_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_character_h (
    history_id bigint NOT NULL,
    book_character_id bigint NOT NULL,
    chapter_id bigint NOT NULL,
    species_id bigint NOT NULL,
    lname character varying(60),
    fname character varying(50),
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    suffix character varying(5),
    nickname character varying(20),
    gender character(1) DEFAULT 'U'::bpchar NOT NULL,
    relevance character(1) DEFAULT 'U'::bpchar NOT NULL,
    affiliation character varying(30),
    occupation character varying(30),
    relationship character varying(30),
    CONSTRAINT bch_gender_chk CHECK (((gender = 'U'::bpchar) OR (gender = 'M'::bpchar) OR (gender = 'F'::bpchar) OR (gender = 'N'::bpchar))),
    CONSTRAINT bch_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar))),
    CONSTRAINT bch_relevance_chk CHECK (((relevance = 'P'::bpchar) OR (relevance = 'S'::bpchar) OR (relevance = 'I'::bpchar) OR (relevance = 'U'::bpchar)))
);


ALTER TABLE public.book_character_h OWNER TO postgres;

--
-- Name: book_character_h_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_character_h_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.book_character_h_history_id_seq OWNER TO postgres;

--
-- Name: book_character_h_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.book_character_h_history_id_seq OWNED BY public.book_character_h.history_id;


--
-- Name: book_character_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_character_log (
    bc_log_id bigint NOT NULL,
    book_character_id bigint NOT NULL,
    chapter_id bigint NOT NULL,
    description character varying(2000),
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    text_description text,
    CONSTRAINT bcl_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.book_character_log OWNER TO postgres;

--
-- Name: book_character_log_bc_log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_character_log_bc_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.book_character_log_bc_log_id_seq OWNER TO postgres;

--
-- Name: book_character_log_bc_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.book_character_log_bc_log_id_seq OWNED BY public.book_character_log.bc_log_id;


--
-- Name: book_character_log_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_character_log_h (
    history_id bigint NOT NULL,
    bc_log_id bigint NOT NULL,
    book_character_id bigint NOT NULL,
    chapter_id bigint NOT NULL,
    description character varying(2000),
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    text_description text,
    CONSTRAINT bclh_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.book_character_log_h OWNER TO postgres;

--
-- Name: book_character_log_h_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_character_log_h_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.book_character_log_h_history_id_seq OWNER TO postgres;

--
-- Name: book_character_log_h_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.book_character_log_h_history_id_seq OWNED BY public.book_character_log_h.history_id;


--
-- Name: book_comment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_comment (
    book_comment_id bigint NOT NULL,
    user_id bigint NOT NULL,
    book_id bigint NOT NULL,
    date_made timestamp without time zone NOT NULL,
    book_comment character varying(2000) NOT NULL
);


ALTER TABLE public.book_comment OWNER TO postgres;

--
-- Name: book_comment_book_comment_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_comment_book_comment_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.book_comment_book_comment_id_seq OWNER TO postgres;

--
-- Name: book_comment_book_comment_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.book_comment_book_comment_id_seq OWNED BY public.book_comment.book_comment_id;


--
-- Name: book_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_h (
    history_id bigint NOT NULL,
    book_id bigint NOT NULL,
    genre_id integer NOT NULL,
    author1_id bigint NOT NULL,
    author2_id bigint,
    author3_id bigint,
    book_title character varying(80) NOT NULL,
    year_published integer DEFAULT 0 NOT NULL,
    description character varying(2000),
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    CONSTRAINT book_rcth_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.book_h OWNER TO postgres;

--
-- Name: book_h_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_h_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.book_h_history_id_seq OWNER TO postgres;

--
-- Name: book_h_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.book_h_history_id_seq OWNED BY public.book_h.history_id;


--
-- Name: book_rating; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_rating (
    book_rating_id bigint NOT NULL,
    user_id bigint NOT NULL,
    book_id bigint NOT NULL,
    rating smallint NOT NULL,
    date_made integer NOT NULL
);


ALTER TABLE public.book_rating OWNER TO postgres;

--
-- Name: book_rating_book_rating_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_rating_book_rating_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.book_rating_book_rating_id_seq OWNER TO postgres;

--
-- Name: book_rating_book_rating_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.book_rating_book_rating_id_seq OWNED BY public.book_rating.book_rating_id;


--
-- Name: book_shelf; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_shelf (
    book_shelf_id bigint NOT NULL,
    user_id bigint NOT NULL,
    shelf_type character(1) NOT NULL,
    seqno smallint NOT NULL,
    book_id bigint NOT NULL,
    CONSTRAINT book_shelf_type_chk CHECK (((shelf_type = 'C'::bpchar) OR (shelf_type = 'A'::bpchar)))
);


ALTER TABLE public.book_shelf OWNER TO postgres;

--
-- Name: book_shelf_book_shelf_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_shelf_book_shelf_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.book_shelf_book_shelf_id_seq OWNER TO postgres;

--
-- Name: book_shelf_book_shelf_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.book_shelf_book_shelf_id_seq OWNED BY public.book_shelf.book_shelf_id;


--
-- Name: chapter; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.chapter (
    chapter_id bigint NOT NULL,
    book_id bigint NOT NULL,
    seqno smallint NOT NULL,
    chapter_designation character varying(20) NOT NULL,
    chapter_name character varying(80),
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    description character varying(2000),
    text_description text,
    CONSTRAINT chapter_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.chapter OWNER TO postgres;

--
-- Name: chapter_chapter_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.chapter_chapter_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.chapter_chapter_id_seq OWNER TO postgres;

--
-- Name: chapter_chapter_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.chapter_chapter_id_seq OWNED BY public.chapter.chapter_id;


--
-- Name: chapter_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.chapter_h (
    history_id bigint NOT NULL,
    chapter_id bigint NOT NULL,
    book_id bigint NOT NULL,
    seqno smallint NOT NULL,
    chapter_designation character varying(20) NOT NULL,
    chapter_name character varying(80),
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    description character varying(2000),
    text_description text,
    CONSTRAINT chapter_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.chapter_h OWNER TO postgres;

--
-- Name: chapter_h_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.chapter_h_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.chapter_h_history_id_seq OWNER TO postgres;

--
-- Name: chapter_h_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.chapter_h_history_id_seq OWNED BY public.chapter_h.history_id;


--
-- Name: genre; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.genre (
    genre_id integer NOT NULL,
    sub_genre_id integer,
    genre_name character varying(80) NOT NULL
);


ALTER TABLE public.genre OWNER TO postgres;

--
-- Name: genre_genre_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.genre_genre_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.genre_genre_id_seq OWNER TO postgres;

--
-- Name: genre_genre_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.genre_genre_id_seq OWNED BY public.genre.genre_id;


--
-- Name: location; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.location (
    location_id bigint NOT NULL,
    inside_location_id bigint,
    chapter_id bigint NOT NULL,
    loc_name character varying(60) NOT NULL,
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    CONSTRAINT location_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.location OWNER TO postgres;

--
-- Name: location_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.location_h (
    history_id bigint NOT NULL,
    location_id bigint NOT NULL,
    inside_location_id bigint,
    chapter_id bigint NOT NULL,
    loc_name character varying(60) NOT NULL,
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    CONSTRAINT locationh_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.location_h OWNER TO postgres;

--
-- Name: location_h_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.location_h_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.location_h_history_id_seq OWNER TO postgres;

--
-- Name: location_h_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.location_h_history_id_seq OWNED BY public.location_h.history_id;


--
-- Name: location_location_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.location_location_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.location_location_id_seq OWNER TO postgres;

--
-- Name: location_location_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.location_location_id_seq OWNED BY public.location.location_id;


--
-- Name: location_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.location_log (
    location_log_id bigint NOT NULL,
    location_id bigint NOT NULL,
    chapter_id bigint NOT NULL,
    description character varying(2000),
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    text_description text,
    CONSTRAINT ll_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.location_log OWNER TO postgres;

--
-- Name: location_log_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.location_log_h (
    history_id bigint NOT NULL,
    location_log_id bigint NOT NULL,
    location_id bigint NOT NULL,
    chapter_id bigint NOT NULL,
    description character varying(2000),
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    text_description text,
    CONSTRAINT llh_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.location_log_h OWNER TO postgres;

--
-- Name: location_log_h_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.location_log_h_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.location_log_h_history_id_seq OWNER TO postgres;

--
-- Name: location_log_h_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.location_log_h_history_id_seq OWNED BY public.location_log_h.history_id;


--
-- Name: location_log_location_log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.location_log_location_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.location_log_location_log_id_seq OWNER TO postgres;

--
-- Name: location_log_location_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.location_log_location_log_id_seq OWNED BY public.location_log.location_log_id;


--
-- Name: login_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.login_history (
    user_id bigint NOT NULL,
    login_date timestamp without time zone,
    ip_address character varying(39)
);


ALTER TABLE public.login_history OWNER TO postgres;

--
-- Name: COLUMN login_history.ip_address; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.login_history.ip_address IS 'Leave enough room for IPv6';


--
-- Name: login_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.login_user (
    user_id bigint NOT NULL,
    user_password character(80) NOT NULL,
    user_fname character varying(20) NOT NULL,
    user_lname character varying(20) NOT NULL,
    email_address character varying(80) NOT NULL,
    birth_date integer NOT NULL,
    sex character(1) DEFAULT 'U'::bpchar NOT NULL,
    when_added timestamp without time zone NOT NULL,
    when_authorized timestamp without time zone,
    administrator character(1) DEFAULT 'N'::bpchar NOT NULL,
    current_uuid character(36),
    uuid_last_used timestamp without time zone,
    new_email character varying(80),
    new_password character(80),
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    user_status character(1) DEFAULT 'A'::bpchar NOT NULL,
    CONSTRAINT login_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar))),
    CONSTRAINT login_user_admin_chk CHECK (((administrator = 'N'::bpchar) OR (administrator = 'Y'::bpchar))),
    CONSTRAINT login_user_sex_chk CHECK (((sex = 'U'::bpchar) OR (sex = 'M'::bpchar) OR (sex = 'F'::bpchar))),
    CONSTRAINT login_user_status_chk CHECK (((user_status = 'A'::bpchar) OR (user_status = 'D'::bpchar) OR (user_status = 'B'::bpchar)))
);


ALTER TABLE public.login_user OWNER TO postgres;

--
-- Name: COLUMN login_user.current_uuid; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.login_user.current_uuid IS 'UUID for this user for the current session only';


--
-- Name: COLUMN login_user.uuid_last_used; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.login_user.uuid_last_used IS 'The date & time current_uuid last used.  Used for aging.';


--
-- Name: COLUMN login_user.new_email; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.login_user.new_email IS 'Used to hold their new email address before they validate it so that the old email address will still work';


--
-- Name: COLUMN login_user.new_password; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.login_user.new_password IS 'Used to hold their new machine generated password before they validate it so that the old email address and pw will still work';


--
-- Name: COLUMN login_user.user_status; Type: COMMENT; Schema: public; Owner: postgres
--

COMMENT ON COLUMN public.login_user.user_status IS 'Active / Deleted / Banned';


--
-- Name: login_user_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.login_user_h (
    history_id bigint NOT NULL,
    user_id bigint NOT NULL,
    user_password character(80) NOT NULL,
    user_fname character varying(20) NOT NULL,
    user_lname character varying(20) NOT NULL,
    email_address character varying(80) NOT NULL,
    birth_date integer NOT NULL,
    sex character(1) DEFAULT 'U'::bpchar NOT NULL,
    when_added timestamp without time zone NOT NULL,
    when_authorized timestamp without time zone,
    administrator character(1) DEFAULT 'N'::bpchar NOT NULL,
    current_uuid character(36),
    uuid_last_used timestamp without time zone,
    new_email character varying(80),
    new_password character(80),
    user_status character(1) DEFAULT 'A'::bpchar NOT NULL,
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    CONSTRAINT loginh_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar))),
    CONSTRAINT loginh_user_admin_chk CHECK (((administrator = 'N'::bpchar) OR (administrator = 'Y'::bpchar))),
    CONSTRAINT loginh_user_sex_chk CHECK (((sex = 'U'::bpchar) OR (sex = 'M'::bpchar) OR (sex = 'F'::bpchar))),
    CONSTRAINT loginh_user_status_chk CHECK (((user_status = 'A'::bpchar) OR (user_status = 'D'::bpchar) OR (user_status = 'B'::bpchar)))
);


ALTER TABLE public.login_user_h OWNER TO postgres;

--
-- Name: login_user_h_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.login_user_h_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.login_user_h_history_id_seq OWNER TO postgres;

--
-- Name: login_user_h_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.login_user_h_history_id_seq OWNED BY public.login_user_h.history_id;


--
-- Name: login_user_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.login_user_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.login_user_user_id_seq OWNER TO postgres;

--
-- Name: login_user_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.login_user_user_id_seq OWNED BY public.login_user.user_id;


--
-- Name: species; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.species (
    species_id bigint NOT NULL,
    chapter_id bigint NOT NULL,
    species_name character varying(30) NOT NULL,
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    CONSTRAINT species_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.species OWNER TO postgres;

--
-- Name: species_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.species_h (
    history_id bigint NOT NULL,
    species_id bigint NOT NULL,
    chapter_id bigint NOT NULL,
    species_name character varying(30) NOT NULL,
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    CONSTRAINT speciesh_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.species_h OWNER TO postgres;

--
-- Name: species_h_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.species_h_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.species_h_history_id_seq OWNER TO postgres;

--
-- Name: species_h_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.species_h_history_id_seq OWNED BY public.species_h.history_id;


--
-- Name: species_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.species_log (
    species_log_id bigint NOT NULL,
    species_id bigint NOT NULL,
    chapter_id bigint NOT NULL,
    description character varying(2000),
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    text_description text,
    CONSTRAINT sl_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.species_log OWNER TO postgres;

--
-- Name: species_log_h; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.species_log_h (
    history_id bigint NOT NULL,
    species_log_id bigint NOT NULL,
    species_id bigint NOT NULL,
    chapter_id bigint NOT NULL,
    description character varying(2000),
    record_change_date timestamp without time zone NOT NULL,
    record_change_type character(1) NOT NULL,
    record_user_id bigint NOT NULL,
    text_description text,
    CONSTRAINT slh_rct_chk CHECK (((record_change_type = 'N'::bpchar) OR (record_change_type = 'M'::bpchar) OR (record_change_type = 'D'::bpchar)))
);


ALTER TABLE public.species_log_h OWNER TO postgres;

--
-- Name: species_log_h_history_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.species_log_h_history_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.species_log_h_history_id_seq OWNER TO postgres;

--
-- Name: species_log_h_history_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.species_log_h_history_id_seq OWNED BY public.species_log_h.history_id;


--
-- Name: species_log_species_log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.species_log_species_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.species_log_species_log_id_seq OWNER TO postgres;

--
-- Name: species_log_species_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.species_log_species_log_id_seq OWNED BY public.species_log.species_log_id;


--
-- Name: species_species_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.species_species_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.species_species_id_seq OWNER TO postgres;

--
-- Name: species_species_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.species_species_id_seq OWNED BY public.species.species_id;


--
-- Name: author author_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author ALTER COLUMN author_id SET DEFAULT nextval('public.author_author_id_seq'::regclass);


--
-- Name: author_h history_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author_h ALTER COLUMN history_id SET DEFAULT nextval('public.author_h_history_id_seq'::regclass);


--
-- Name: book book_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book ALTER COLUMN book_id SET DEFAULT nextval('public.book_book_id_seq'::regclass);


--
-- Name: book_character book_character_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_character ALTER COLUMN book_character_id SET DEFAULT nextval('public.book_character_book_character_id_seq'::regclass);


--
-- Name: book_character_h history_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_character_h ALTER COLUMN history_id SET DEFAULT nextval('public.book_character_h_history_id_seq'::regclass);


--
-- Name: book_character_log bc_log_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_character_log ALTER COLUMN bc_log_id SET DEFAULT nextval('public.book_character_log_bc_log_id_seq'::regclass);


--
-- Name: book_character_log_h history_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_character_log_h ALTER COLUMN history_id SET DEFAULT nextval('public.book_character_log_h_history_id_seq'::regclass);


--
-- Name: book_comment book_comment_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_comment ALTER COLUMN book_comment_id SET DEFAULT nextval('public.book_comment_book_comment_id_seq'::regclass);


--
-- Name: book_h history_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_h ALTER COLUMN history_id SET DEFAULT nextval('public.book_h_history_id_seq'::regclass);


--
-- Name: book_rating book_rating_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_rating ALTER COLUMN book_rating_id SET DEFAULT nextval('public.book_rating_book_rating_id_seq'::regclass);


--
-- Name: book_shelf book_shelf_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_shelf ALTER COLUMN book_shelf_id SET DEFAULT nextval('public.book_shelf_book_shelf_id_seq'::regclass);


--
-- Name: chapter chapter_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chapter ALTER COLUMN chapter_id SET DEFAULT nextval('public.chapter_chapter_id_seq'::regclass);


--
-- Name: chapter_h history_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chapter_h ALTER COLUMN history_id SET DEFAULT nextval('public.chapter_h_history_id_seq'::regclass);


--
-- Name: genre genre_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.genre ALTER COLUMN genre_id SET DEFAULT nextval('public.genre_genre_id_seq'::regclass);


--
-- Name: location location_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location ALTER COLUMN location_id SET DEFAULT nextval('public.location_location_id_seq'::regclass);


--
-- Name: location_h history_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_h ALTER COLUMN history_id SET DEFAULT nextval('public.location_h_history_id_seq'::regclass);


--
-- Name: location_log location_log_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_log ALTER COLUMN location_log_id SET DEFAULT nextval('public.location_log_location_log_id_seq'::regclass);


--
-- Name: location_log_h history_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_log_h ALTER COLUMN history_id SET DEFAULT nextval('public.location_log_h_history_id_seq'::regclass);


--
-- Name: login_user user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.login_user ALTER COLUMN user_id SET DEFAULT nextval('public.login_user_user_id_seq'::regclass);


--
-- Name: login_user_h history_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.login_user_h ALTER COLUMN history_id SET DEFAULT nextval('public.login_user_h_history_id_seq'::regclass);


--
-- Name: species species_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species ALTER COLUMN species_id SET DEFAULT nextval('public.species_species_id_seq'::regclass);


--
-- Name: species_h history_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species_h ALTER COLUMN history_id SET DEFAULT nextval('public.species_h_history_id_seq'::regclass);


--
-- Name: species_log species_log_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species_log ALTER COLUMN species_log_id SET DEFAULT nextval('public.species_log_species_log_id_seq'::regclass);


--
-- Name: species_log_h history_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species_log_h ALTER COLUMN history_id SET DEFAULT nextval('public.species_log_h_history_id_seq'::regclass);


--
-- Name: author_h author_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author_h
    ADD CONSTRAINT author_h_pkey PRIMARY KEY (history_id);


--
-- Name: author author_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author
    ADD CONSTRAINT author_pkey PRIMARY KEY (author_id);


--
-- Name: book_character_h book_character_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_character_h
    ADD CONSTRAINT book_character_h_pkey PRIMARY KEY (history_id);


--
-- Name: book_character_log_h book_character_log_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_character_log_h
    ADD CONSTRAINT book_character_log_h_pkey PRIMARY KEY (history_id);


--
-- Name: book_character_log book_character_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_character_log
    ADD CONSTRAINT book_character_log_pkey PRIMARY KEY (bc_log_id);


--
-- Name: book_character book_character_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_character
    ADD CONSTRAINT book_character_pkey PRIMARY KEY (book_character_id);


--
-- Name: book_comment book_comment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_comment
    ADD CONSTRAINT book_comment_pkey PRIMARY KEY (book_comment_id);


--
-- Name: book_h book_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_h
    ADD CONSTRAINT book_h_pkey PRIMARY KEY (history_id);


--
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (book_id);


--
-- Name: book_rating book_rating_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_rating
    ADD CONSTRAINT book_rating_pkey PRIMARY KEY (book_rating_id);


--
-- Name: book_shelf book_shelf_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_shelf
    ADD CONSTRAINT book_shelf_pkey PRIMARY KEY (book_shelf_id);


--
-- Name: chapter_h chapter_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chapter_h
    ADD CONSTRAINT chapter_h_pkey PRIMARY KEY (history_id);


--
-- Name: chapter chapter_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chapter
    ADD CONSTRAINT chapter_pkey PRIMARY KEY (chapter_id);


--
-- Name: genre genre_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.genre
    ADD CONSTRAINT genre_pkey PRIMARY KEY (genre_id);


--
-- Name: location_h location_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_h
    ADD CONSTRAINT location_h_pkey PRIMARY KEY (history_id);


--
-- Name: location_log_h location_log_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_log_h
    ADD CONSTRAINT location_log_h_pkey PRIMARY KEY (history_id);


--
-- Name: location_log location_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_log
    ADD CONSTRAINT location_log_pkey PRIMARY KEY (location_log_id);


--
-- Name: location location_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location
    ADD CONSTRAINT location_pkey PRIMARY KEY (location_id);


--
-- Name: login_user_h login_user_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.login_user_h
    ADD CONSTRAINT login_user_h_pkey PRIMARY KEY (history_id);


--
-- Name: login_user login_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.login_user
    ADD CONSTRAINT login_user_pkey PRIMARY KEY (user_id);


--
-- Name: species_h species_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species_h
    ADD CONSTRAINT species_h_pkey PRIMARY KEY (history_id);


--
-- Name: species_log_h species_log_h_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species_log_h
    ADD CONSTRAINT species_log_h_pkey PRIMARY KEY (history_id);


--
-- Name: species_log species_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species_log
    ADD CONSTRAINT species_log_pkey PRIMARY KEY (species_log_id);


--
-- Name: species species_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species
    ADD CONSTRAINT species_pkey PRIMARY KEY (species_id);


--
-- Name: author_h_author_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX author_h_author_idx ON public.author_h USING btree (author_id);


--
-- Name: author_name_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX author_name_idx ON public.author USING btree (lname, fname);


--
-- Name: author_rech_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX author_rech_user_idx ON public.author_h USING btree (record_user_id);


--
-- Name: author_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX author_record_user_idx ON public.author USING btree (record_user_id);


--
-- Name: bc_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX bc_record_user_idx ON public.book_character USING btree (record_user_id);


--
-- Name: bc_when_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX bc_when_idx ON public.book_character USING btree (chapter_id);


--
-- Name: bch_book_character_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX bch_book_character_idx ON public.book_character_h USING btree (book_character_id);


--
-- Name: bch_log_chap_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX bch_log_chap_idx ON public.book_character_log_h USING btree (chapter_id);


--
-- Name: bch_log_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX bch_log_pidx ON public.book_character_log_h USING btree (book_character_id);


--
-- Name: bch_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX bch_record_user_idx ON public.book_character_h USING btree (record_user_id);


--
-- Name: bch_when_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX bch_when_idx ON public.book_character_h USING btree (chapter_id);


--
-- Name: bcl_h_bc_log_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX bcl_h_bc_log_idx ON public.book_character_log_h USING btree (bc_log_id);


--
-- Name: bcl_log_chap_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX bcl_log_chap_idx ON public.book_character_log USING btree (chapter_id);


--
-- Name: bcl_log_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX bcl_log_pidx ON public.book_character_log USING btree (book_character_id, chapter_id);


--
-- Name: bcl_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX bcl_record_user_idx ON public.book_character_log USING btree (record_user_id);


--
-- Name: bclh_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX bclh_record_user_idx ON public.book_character_log_h USING btree (record_user_id);


--
-- Name: book_author1_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX book_author1_idx ON public.book USING btree (author1_id);


--
-- Name: book_author2_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX book_author2_idx ON public.book USING btree (author2_id);


--
-- Name: book_author3_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX book_author3_idx ON public.book USING btree (author3_id);


--
-- Name: book_char_species_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX book_char_species_idx ON public.book_character USING btree (species_id);


--
-- Name: book_charh_species_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX book_charh_species_idx ON public.book_character_h USING btree (species_id);


--
-- Name: book_comment_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX book_comment_pidx ON public.book_comment USING btree (book_id, date_made, user_id);


--
-- Name: book_comment_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX book_comment_user_idx ON public.book_comment USING btree (user_id, date_made);


--
-- Name: book_genre_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX book_genre_idx ON public.book USING btree (genre_id);


--
-- Name: book_h_book_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX book_h_book_idx ON public.book_h USING btree (book_id);


--
-- Name: book_rating_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX book_rating_pidx ON public.book_rating USING btree (book_id, date_made, user_id);


--
-- Name: book_rating_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX book_rating_user_idx ON public.book_rating USING btree (user_id, date_made);


--
-- Name: book_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX book_record_user_idx ON public.book USING btree (record_user_id);


--
-- Name: book_shelf_book_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX book_shelf_book_idx ON public.book_shelf USING btree (book_id);


--
-- Name: book_shelf_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX book_shelf_user_idx ON public.book_shelf USING btree (user_id, shelf_type, seqno);


--
-- Name: book_title_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX book_title_idx ON public.book USING btree (book_title);


--
-- Name: bookh_author1_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX bookh_author1_idx ON public.book_h USING btree (author1_id);


--
-- Name: bookh_author2_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX bookh_author2_idx ON public.book_h USING btree (author2_id);


--
-- Name: bookh_author3_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX bookh_author3_idx ON public.book_h USING btree (author3_id);


--
-- Name: bookh_genre_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX bookh_genre_idx ON public.book_h USING btree (genre_id);


--
-- Name: bookh_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX bookh_record_user_idx ON public.book_h USING btree (record_user_id);


--
-- Name: chapter_h_chapter_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX chapter_h_chapter_idx ON public.chapter_h USING btree (chapter_id);


--
-- Name: chapter_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX chapter_pidx ON public.chapter USING btree (book_id, seqno);


--
-- Name: chapter_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX chapter_record_user_idx ON public.chapter USING btree (record_user_id);


--
-- Name: chapterh_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX chapterh_pidx ON public.chapter_h USING btree (book_id);


--
-- Name: chapterh_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX chapterh_record_user_idx ON public.chapter_h USING btree (record_user_id);


--
-- Name: genre_name_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX genre_name_idx ON public.genre USING btree (genre_name);


--
-- Name: genre_sub_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX genre_sub_idx ON public.genre USING btree (sub_genre_id);


--
-- Name: ll_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX ll_record_user_idx ON public.location_log USING btree (record_user_id);


--
-- Name: llh_location_log_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX llh_location_log_idx ON public.location_log_h USING btree (location_log_id);


--
-- Name: llh_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX llh_record_user_idx ON public.location_log_h USING btree (record_user_id);


--
-- Name: location_h_location_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX location_h_location_idx ON public.location_h USING btree (location_id);


--
-- Name: location_inside_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX location_inside_idx ON public.location USING btree (inside_location_id);


--
-- Name: location_log_chap_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX location_log_chap_idx ON public.location_log USING btree (chapter_id);


--
-- Name: location_log_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX location_log_pidx ON public.location_log USING btree (location_id, chapter_id);


--
-- Name: location_logh_chap_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX location_logh_chap_idx ON public.location_log_h USING btree (chapter_id);


--
-- Name: location_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX location_record_user_idx ON public.location USING btree (record_user_id);


--
-- Name: location_when_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX location_when_idx ON public.location USING btree (chapter_id);


--
-- Name: locationh_inside_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX locationh_inside_idx ON public.location_h USING btree (inside_location_id);


--
-- Name: locationh_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX locationh_record_user_idx ON public.location_h USING btree (record_user_id);


--
-- Name: locationh_when_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX locationh_when_idx ON public.location_h USING btree (chapter_id);


--
-- Name: login_history_log_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX login_history_log_idx ON public.login_history USING btree (login_date);


--
-- Name: login_history_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX login_history_user_idx ON public.login_history USING btree (user_id, login_date);


--
-- Name: login_user_h_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX login_user_h_record_user_idx ON public.login_user_h USING btree (record_user_id);


--
-- Name: login_user_new_email_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX login_user_new_email_idx ON public.login_user USING btree (new_email);


--
-- Name: login_user_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX login_user_record_user_idx ON public.login_user USING btree (record_user_id);


--
-- Name: login_user_uuid_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX login_user_uuid_idx ON public.login_user USING btree (current_uuid);


--
-- Name: sl_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX sl_record_user_idx ON public.species_log USING btree (record_user_id);


--
-- Name: slh_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX slh_record_user_idx ON public.species_log_h USING btree (record_user_id);


--
-- Name: slh_species_log_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX slh_species_log_idx ON public.species_log_h USING btree (species_log_id);


--
-- Name: species_h_species_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX species_h_species_idx ON public.species_h USING btree (species_id);


--
-- Name: species_log_chap_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX species_log_chap_idx ON public.species_log USING btree (chapter_id);


--
-- Name: species_log_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX species_log_pidx ON public.species_log USING btree (species_id, chapter_id);


--
-- Name: species_logh_chap_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX species_logh_chap_idx ON public.species_log_h USING btree (chapter_id);


--
-- Name: species_logh_pidx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX species_logh_pidx ON public.species_log_h USING btree (species_id);


--
-- Name: species_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX species_record_user_idx ON public.species USING btree (record_user_id);


--
-- Name: species_when_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX species_when_idx ON public.species USING btree (chapter_id);


--
-- Name: speciesh_record_user_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX speciesh_record_user_idx ON public.species_h USING btree (record_user_id);


--
-- Name: speciesh_when_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX speciesh_when_idx ON public.species_h USING btree (chapter_id);


--
-- Name: user_email_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX user_email_idx ON public.login_user USING btree (lower((email_address)::text));


--
-- Name: user_name_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE UNIQUE INDEX user_name_idx ON public.login_user USING btree (lower((user_lname)::text), lower((user_fname)::text), user_id);


--
-- Name: author_h author_rech_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author_h
    ADD CONSTRAINT author_rech_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: author author_record_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author
    ADD CONSTRAINT author_record_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: book_character bc_record_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_character
    ADD CONSTRAINT bc_record_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: book_character bc_when_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_character
    ADD CONSTRAINT bc_when_fkey FOREIGN KEY (chapter_id) REFERENCES public.chapter(chapter_id);


--
-- Name: book_character_h bch_record_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_character_h
    ADD CONSTRAINT bch_record_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: book_character_log bcl_log_book_character_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_character_log
    ADD CONSTRAINT bcl_log_book_character_fkey FOREIGN KEY (book_character_id) REFERENCES public.book_character(book_character_id);


--
-- Name: book_character_log bcl_log_chap_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_character_log
    ADD CONSTRAINT bcl_log_chap_fkey FOREIGN KEY (chapter_id) REFERENCES public.chapter(chapter_id);


--
-- Name: book_character_log bcl_record_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_character_log
    ADD CONSTRAINT bcl_record_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: book_character_log_h bclh_record_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_character_log_h
    ADD CONSTRAINT bclh_record_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: book book_author1_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_author1_fkey FOREIGN KEY (author1_id) REFERENCES public.author(author_id);


--
-- Name: book book_author2_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_author2_fkey FOREIGN KEY (author2_id) REFERENCES public.author(author_id);


--
-- Name: book book_author3_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_author3_fkey FOREIGN KEY (author3_id) REFERENCES public.author(author_id);


--
-- Name: book_character book_character_species_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_character
    ADD CONSTRAINT book_character_species_fkey FOREIGN KEY (species_id) REFERENCES public.species(species_id);


--
-- Name: book_comment book_comment_book_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_comment
    ADD CONSTRAINT book_comment_book_fkey FOREIGN KEY (book_id) REFERENCES public.book(book_id);


--
-- Name: book_comment book_comment_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_comment
    ADD CONSTRAINT book_comment_user_fkey FOREIGN KEY (user_id) REFERENCES public.login_user(user_id);


--
-- Name: book book_genre_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_genre_fkey FOREIGN KEY (genre_id) REFERENCES public.genre(genre_id);


--
-- Name: book_rating book_rating_book_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_rating
    ADD CONSTRAINT book_rating_book_fkey FOREIGN KEY (book_id) REFERENCES public.book(book_id);


--
-- Name: book_rating book_rating_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_rating
    ADD CONSTRAINT book_rating_user_fkey FOREIGN KEY (user_id) REFERENCES public.login_user(user_id);


--
-- Name: book book_record_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_record_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: book_shelf book_shelf_book_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_shelf
    ADD CONSTRAINT book_shelf_book_fkey FOREIGN KEY (book_id) REFERENCES public.book(book_id);


--
-- Name: book_shelf book_shelf_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_shelf
    ADD CONSTRAINT book_shelf_user_fkey FOREIGN KEY (user_id) REFERENCES public.login_user(user_id);


--
-- Name: book_h bookh_record_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_h
    ADD CONSTRAINT bookh_record_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: chapter chapter_book_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chapter
    ADD CONSTRAINT chapter_book_fkey FOREIGN KEY (book_id) REFERENCES public.book(book_id);


--
-- Name: chapter chapter_record_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chapter
    ADD CONSTRAINT chapter_record_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: chapter_h chapterh_record_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chapter_h
    ADD CONSTRAINT chapterh_record_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: genre genre_sub_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.genre
    ADD CONSTRAINT genre_sub_fkey FOREIGN KEY (sub_genre_id) REFERENCES public.genre(genre_id);


--
-- Name: location_log ll_record_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_log
    ADD CONSTRAINT ll_record_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: location_log_h llh_record_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_log_h
    ADD CONSTRAINT llh_record_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: location location_location_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location
    ADD CONSTRAINT location_location_fkey FOREIGN KEY (inside_location_id) REFERENCES public.location(location_id);


--
-- Name: location_log location_log_chap_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_log
    ADD CONSTRAINT location_log_chap_fkey FOREIGN KEY (chapter_id) REFERENCES public.chapter(chapter_id);


--
-- Name: location_log location_log_location_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_log
    ADD CONSTRAINT location_log_location_fkey FOREIGN KEY (location_id) REFERENCES public.location(location_id);


--
-- Name: location location_record_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location
    ADD CONSTRAINT location_record_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: location location_when_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location
    ADD CONSTRAINT location_when_fkey FOREIGN KEY (chapter_id) REFERENCES public.chapter(chapter_id);


--
-- Name: location_h locationh_record_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.location_h
    ADD CONSTRAINT locationh_record_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: login_history login_history_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.login_history
    ADD CONSTRAINT login_history_user_fkey FOREIGN KEY (user_id) REFERENCES public.login_user(user_id);


--
-- Name: login_user login_user_rec_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.login_user
    ADD CONSTRAINT login_user_rec_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: login_user_h login_user_rech_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.login_user_h
    ADD CONSTRAINT login_user_rech_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: species_log sl_record_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species_log
    ADD CONSTRAINT sl_record_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: species_log_h slh_record_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species_log_h
    ADD CONSTRAINT slh_record_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: species_log species_log_chap_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species_log
    ADD CONSTRAINT species_log_chap_fkey FOREIGN KEY (chapter_id) REFERENCES public.chapter(chapter_id);


--
-- Name: species_log species_log_species_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species_log
    ADD CONSTRAINT species_log_species_fkey FOREIGN KEY (species_id) REFERENCES public.species(species_id);


--
-- Name: species species_record_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species
    ADD CONSTRAINT species_record_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- Name: species species_when_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species
    ADD CONSTRAINT species_when_fkey FOREIGN KEY (chapter_id) REFERENCES public.chapter(chapter_id);


--
-- Name: species_h speciesh_record_user_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.species_h
    ADD CONSTRAINT speciesh_record_user_fkey FOREIGN KEY (record_user_id) REFERENCES public.login_user(user_id);


--
-- PostgreSQL database dump complete
--

