using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;

namespace TourLoggerService
{
    [DataContract]
    public class Users
    {
        [DataMember] 
        public int user_id { get; set; }
        [DataMember] 
        public string user_name { get; set; }
        [DataMember] 
        public string password { get; set; }
        [DataMember] 
        public string first_name { get; set; }
        [DataMember] 
        public string last_name { get; set; }
        [DataMember] 
        public string email { get; set; }
        [DataMember] 
        public string phoneno { get; set; }
        [DataMember] 
        public string role { get; set; }
    }
}