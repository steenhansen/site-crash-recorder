﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿### Rational ﻿﻿﻿Crash-sms is a Clojure program that runs on Heroku at https://fathomless-woodland-85635.herokuapp.com/ which checks a list of websites every 2 hours and texts my phone when a website does not conform to a valid website. Non cached website pages are checked for database, WordPress, and Node.js crashes. The Heroku add-on Till Mobile handles the sms messages, while Temporize Scheduler calls the cron jobs.### Compile crash-sms in Emacs with Cider```M-x load-file /crash-sms/src/core.cljM-x cider-jack-inM-x cider-ns-refreshM-x cider-repl-set-ns```### Run crash-sms in Emacs with Cider```core> (-local-main USE_FAKE_DB LOCAL_CONFIG)```### View last two months website data```http://localhost:8080/```### Launch cron job to check web pages```http://localhost:8080/url-for-cron-execution```