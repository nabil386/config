// Learn more about configuring this file at <https://github.com/theintern/intern/wiki/Configuring-Intern>.
// These default settings work OK for most people. The options that *must* be changed below are the
// packages, suites, excludeInstrumentation, and (if you want functional tests) functionalSuites.
define({
        // The port on which the instrumenting proxy will listen
        proxyPort: 9000,

        // A fully qualified URL to the Intern proxy
        proxyUrl: 'http://localhost:9000/',

        // Default desired capabilities for all environments. Individual capabilities can be overridden by any of the
        // specified browser environments in the `environments` array below as well. See
        // https://code.google.com/p/selenium/wiki/DesiredCapabilities for standard Selenium capabilities and
        // https://saucelabs.com/docs/additional-config#desired-capabilities for Sauce Labs capabilities.
        // Note that the `build` capability will be filled in with the current commit ID from the Travis CI environment
        // automatically
        capabilities: {
                'selenium-version': '5.0.0'
        },

        // Browsers to run integration testing against. Note that version numbers must be strings if used with Sauce
        // OnDemand. Options that will be permutated are browserName, version, platform, and platformVersion; any other
        // capabilities options specified for an environment will be copied as-is
        environments: [
                //{ browserName: "firefox"}
                { browserName: "internet explorer", platform: 'WINDOWS'},
                { browserName: "chrome", platform: 'UNIX'},
                { browserName: "chrome", platform: 'MAC'}
                //{ browserName: 'internet explorer', version: '9', platform: 'Windows 7' },

                /*
                { browserName: 'internet explorer', version: '10', platform: 'Windows 8' }
                { browserName: 'internet explorer', version: '9', platform: 'Windows 7' },
                { browserName: 'firefox', version: '23', platform: [ 'Linux', 'Windows 7' ] },
                { browserName: 'firefox', version: '21', platform: 'Mac 10.6' },
                { browserName: 'chrome', platform: [ 'Linux', 'Mac 10.8', 'Windows 7' ] },
                { browserName: 'safari', version: '6', platform: 'Mac 10.8' }
                */
        ],

        // Maximum number of simultaneous integration tests that should be executed on the remote WebDriver service
        maxConcurrency: 3,

        // Whether or not to start Sauce Connect before running tests
        useSauceConnect: false,

        // Connection information for the remote WebDriver service. If using Sauce Labs, keep your username and password
        // in the SAUCE_USERNAME and SAUCE_ACCESS_KEY environment variables unless you are sure you will NEVER be
        // publishing this configuration file somewhere
        webdriver: {
                host: 'localhost',
                port: 4444,
                //host: 'ondemand.saucelabs.com',
                //port: 80,
                //username: "pwd",
                //accessKey: "11111111-1111-1111-1111-111111111111"
        },

        // Configuration options for the module loader; any AMD configuration options supported by the Dojo loader can be
        // used here
       loaderOptions: {
         // Packages that should be registered with the loader in each testing environment
         // //{ name: 'app', location: 'app' }
         packages: [
           { name: 'dojo', location: '../dojo-source/dojo' },
           { name: 'dijit', location: '../dojo-source/dijit' },
           { name: 'dojox', location: '../dojo-source/dojox' },
           { name: 'ieg', location: 'jscript/src/ieg' },
           { name: 'tests', location: 'jscript/tests' },
           { name: 'mocks', location: 'jscript/tests/mocks' }
         ],
         map: {
          '*': {
          'cm/_base/_dom': 'mocks/cm/_base/_dom_mock',
          'curam/debug': 'mocks/curam/debug_mock',
          'curam/util': 'mocks/curam/util_mock',
          'curam/define': 'mocks/curam/define_mock',
          'idx/widget/Dialog': 'mocks/idx/widget/Dialog_mock',
          'idx/widget/HoverHelpTooltip': 'mocks/idx/widget/HoverHelpTooltip_mock',
          'idx/form/FilteringSelect': 'mocks/idx/form/IDXFormFilteringSelect_mock',
          'idx/form/Select': 'mocks/idx/form/Select_mock',
          'idx/form/Textarea': 'mocks/idx/form/Textarea_mock',
          'idx/form/CheckBox': 'mocks/idx/form/CheckBox_mock',
          'idx/form/DateTextBox': 'mocks/idx/form/DateTextBox_mock',
          'curam/util/LocalConfig': 'mocks/curam/util/LocalConfig_mock',
          'curam/widget/ProgressSpinner': 'mocks/curam/widget/ProgressSpinner_mock',
          'curam/widget/IDXFilteringSelect': 'mocks/curam/widget/IDXFilteringSelect_mock',
          'dojo/_base/xhr': 'mocks/dojo/_base/xhr_mock',
          'curam/widget/FilteringSelect': 'mocks/curam/widget/FilteringSelect_mock',
          'curam/widget/_ComboBoxMenu': 'mocks/curam/widget/_ComboBoxMenu_mock',
          'curam/widget/ComboBoxMixin': 'mocks/curam/widget/ComboBoxMixin_mock',
          'curam/util/ResourceBundle' : 'mocks/curam/util/ResourceBundle_mock',
          'ieg/ieg-bootstrap': 'mocks/ieg/ieg-bootstrap_mock'
          }
        }

       },

        // Non-functional test suite(s) to run in each browser
        suites: [ '/jscript/tests/functional/IntelligentEvidenceGatheringTestSuite.js' ],

        // Functional test suite(s) to run in each browser once non-functional tests are completed
        functionalSuites: [ /* 'myPackage/tests/functional'  'tests/functional/index' */ ],

        // A regular expression matching URLs to files that should not be included in code coverage analysis
        //excludeInstrumentation: /^tests\//
        excludeInstrumentation: /dojotk|test|intern-config.js|nls/,

        //reporters: ['console', 'junit','lcovhtml']

});
