define({"topics" : [{"title":"Prerequisites","href":"datacollector\/UserGuide\/Configuration\/HTTP_protocols.html#concept_tcv_5m5_52b","attributes": {"data-id":"concept_tcv_5m5_52b",},"menu": {"hasChildren":false,},"tocID":"concept_tcv_5m5_52b-d46e16382","topics":[]},{"title":"Step 1. Create Keystore Files","shortdesc":"\n               <p class=\"shortdesc\">Create a keystore file that includes each private key and public certificate pair         signed by the CA. A keystore is\n                  used to verify the identity of the client upon a request         from an SSL\/TLS server.\n               </p>\n            ","href":"datacollector\/UserGuide\/Configuration\/HTTP_protocols.html#task_lch_xn5_52b","attributes": {"data-id":"task_lch_xn5_52b",},"menu": {"hasChildren":false,},"tocID":"task_lch_xn5_52b-d46e16404","topics":[]},{"title":"Step 2. Create a Truststore File","shortdesc":"\n               <p class=\"shortdesc\">A truststore file contains certificates from trusted CAs that an SSL\/TLS client uses         to verify the identity of an\n                  SSL\/TLS server. \n               </p>\n            ","href":"datacollector\/UserGuide\/Configuration\/HTTP_protocols.html#task_hzm_ht5_52b","attributes": {"data-id":"task_hzm_ht5_52b",},"menu": {"hasChildren":false,},"tocID":"task_hzm_ht5_52b-d46e16439","topics":[]},{"title":"Step 3. Configure Data Collector to Use HTTPS","shortdesc":"\n               <p class=\"shortdesc\">Modify the <span class=\"ph\">Data Collector</span>         configuration file, <span class=\"ph filepath\">sdc.properties</span>, to configure <span class=\"ph\">Data Collector</span> to use         a secure port and your keystore file. If you created a custom truststore file or modified a         copy of\n                  the default Java truststore file, configure <span class=\"ph\">Data Collector</span> to use         that truststore file.\n               </p>\n            ","href":"datacollector\/UserGuide\/Configuration\/HTTP_protocols.html#task_mhn_j55_52b","attributes": {"data-id":"task_mhn_j55_52b",},"menu": {"hasChildren":false,},"tocID":"task_mhn_j55_52b-d46e16484","topics":[]},{"title":"Step 4. Configure Cluster Pipelines to Use HTTPS","shortdesc":"\n               <p class=\"shortdesc\">To enable HTTPS for cluster pipelines, configure the gateway and worker nodes in the         cluster to use HTTPS. If you\n                  do not run cluster pipelines, you can skip this         step.\n               </p>\n            ","href":"datacollector\/UserGuide\/Configuration\/HTTP_protocols.html#task_wqf_c1w_52b","attributes": {"data-id":"task_wqf_c1w_52b",},"menu": {"hasChildren":false,},"tocID":"task_wqf_c1w_52b-d46e16551","topics":[]}]});