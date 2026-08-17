-- Run this only if you have not already created the tables.
create extension if not exists pgcrypto;
create table if not exists public.employees (
 id uuid primary key default gen_random_uuid(),
 employee_id text unique not null,
 name text not null,
 pin text not null,
 week_off text not null check (week_off in ('Monday','Tuesday','Wednesday','Thursday','Friday','Saturday','Sunday')),
 active boolean not null default true,
 created_at timestamptz not null default now()
);
create table if not exists public.attendance (
 id uuid primary key default gen_random_uuid(),
 employee_id uuid not null references public.employees(id) on delete cascade,
 attendance_date date not null,
 check_in text, check_out text, status text not null,
 check_in_lat double precision, check_in_lng double precision, check_in_accuracy double precision,
 check_out_lat double precision, check_out_lng double precision, check_out_accuracy double precision,
 created_at timestamptz not null default now(),
 unique(employee_id,attendance_date)
);
alter table public.employees enable row level security;
alter table public.attendance enable row level security;
