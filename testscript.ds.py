from selenium import selenium
import unittest, time, re

class Untitled(unittest.TestCase):
    def setUp(self):
        self.verificationErrors = []
        self.selenium = selenium("localhost", 5555, "*firefox", "http://www.apple.com")
        self.selenium.start()
    
    def test_untitled(self):
        sel = self.selenium
        sel.open("/");
        sel.click("//nav[@id='ac-globalnav']/div/ul[2]/li[2]/a")
        sel.click("//nav[@id='ac-globalnav']/div/ul[2]/li[9]/a")
        sel.type("//input[@id='ac-gn-searchform-input']", "hjdhfd")
        sel.click("//button[@id='ac-gn-searchform-submit']")
        sel.click("//nav[@id='ac-globalnav']/div/ul[2]/li[7]/a")
        sel.click("//nav[@id='ac-globalnav']/div/ul[2]/li[4]/a")
        sel.click("//nav[@id='ac-globalnav']/div/ul[2]/li[2]/a")
        try:
            self.failUnless(sel.is_element_present("//div[@id='main']/section[2]/div/div[1]/h2"))
        except AssertionError, e:
            self.verificationErrors.append(str(e))
    
    def tearDown(self):
        self.selenium.stop()
        self.assertEqual([], self.verificationErrors)

if __name__ == "__main__":
    unittest.main()
