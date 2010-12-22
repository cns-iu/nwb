'''
Created on Jul 9, 2009

@author: cdtank
'''

from xml.dom.minidom import parse 
import urllib 

state_list = open("us_states_list.txt")
state_geo_code = open("us_states_geo_code.txt","w")


country_list = open("countries_list_iso_3166_1_2.txt")
country_geo_code = open("countries_geo_code.txt","w") 


class Geocoder:
    """ 
    look up an location using the Yahoo geocoding api
    Requires a Yahoo appid which can be obtained at:
    http://developer.yahoo.net/faq/index.html#appid
    Documentation for the Yahoo geocoding api can be found at:
    http://developer.yahoo.net/maps/rest/V1/geocode.html
    """      

    def __init__(self, appid, address_str, output_file_handler):
        self.addressstr = address_str[0]         
        self.addresses = []
        self.resultcount = 0         
        parms = {'appid': appid, 'location': address_str[0]}
    
        try:
            url = 'http://api.local.yahoo.com/MapsService/V1/geocode?'+urllib.urlencode(parms)
            # parse the xml contents of the url into a dom
            dom = parse(urllib.urlopen(url))
            results = dom.getElementsByTagName('Result')
            self.result_count = len(results)
            for result in results:
                d = {'precision': result.getAttribute('precision'),
                    'warning': result.getAttribute('warning')}
    
            for itm in result.childNodes:
                # if precision is zip, Address childNode will not exist
                if itm.childNodes:
                    d[itm.nodeName] = itm.childNodes[0].data                     
                else:
                    d[itm.nodeName] = ''                
                self.addresses.append(d)
            try:
                output_line = address_str[0].upper(),";",address_str[1],";",self.addresses[0]['Latitude'],";",self.addresses[0]['Longitude']
                output_file_handler.writelines(output_line)
                output_file_handler.writelines("\n")
                print self.addresses
            except:
                raise Exception    
        except:
            pass
        
if __name__ == "__main__":
    """
    The countries & states present in the country or state exclusion list need to be manually updated in 
    the geo code list. These will have faulty values in the final output files. 
    """
    state_exceptions = ['Washington']
    country_exceptions = ['Anguilla','Montserrat','Pitcairn','Saint Helena']
    
#    Put your own yahoo developer app id here.
    yahoo_developer_app_id = 'xxx'
    
    
    for index, state in enumerate(state_list):
            g = Geocoder(yahoo_developer_app_id, state.rstrip().split(";"), state_geo_code)
            print '-'*80
    state_geo_code.close()    
        
    for index, country in enumerate(country_list):
            g = Geocoder(yahoo_developer_app_id, country.rstrip().split(";"), country_geo_code)
            print '-'*80
    country_geo_code.close()
    