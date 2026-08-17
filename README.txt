MESSHO SC ONLINE ATTENDANCE
1. Supabase database and 25 staff are already created.
2. index.html is the staff attendance page.
3. admin.html is the TL dashboard.
4. The Supabase URL and publishable key are already inserted.
5. Host these files on HTTPS (GitHub Pages is one option).
6. Staff use index.html. TL uses admin.html.
7. Create the TL admin account in Supabase Authentication > Users.
8. The supplied URL had /rest/v1/ at the end; the app correctly uses the base project URL.
Security note: this is a starter/prototype. For a real company deployment, use per-employee authentication and stricter RLS/Edge Functions rather than public PIN lookup.
