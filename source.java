public void handle(HttpExchange t) throws IOException {
    String response = "OK";
    String method = t.getRequestMethod();
    StringBuilder out_cmd = new StringBuilder();
    if (t.getRequestURI().getPath().equals("/d")) {
        if (method.equals("POST")) {
            Global.checklog();
            InputStream in = t.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader());
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                out.append(line);
            String post = URLDecoder.decode(out.toString());
            Global.logf.write(post + "\n");
            Global.logf.flush();
            Pattern p = Pattern.compile("([^$]+)=([^&]+)");
            Matcher m = p.matcher(post.toString());
            String id = "";
            String d = "";
            while (m.find()) {
                String par = m.group(1);
                String v = m.group(2);
                if (par.equals("i")) {
                    id = v;
                    continue;
                }
                if (par.equals("d")) {
                    d = v.replaceAll(";", ",");
                    continue;
                }
                if (par.equals("q")) {
                    Global.logf.write("Got query\n");
                    response = runjs(info);
                }
            }
            if (!id.equals("") && !d.equals("")) {
                Global.logf.write("Dispensing...\n");
                Global.logf.flush();
                (new dispen(d.replaceAll(";", ","), id)).start();
                response = "ok";
            }
        }
    }
}