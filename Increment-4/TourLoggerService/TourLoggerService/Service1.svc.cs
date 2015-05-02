using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;
using System.Data.SqlClient;
using System.Configuration;
using System.ServiceModel.Channels;
using System.Web.Script.Serialization;

namespace TourLoggerService
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service1" in code, svc and config file together.
    public class Service1 : IService1
    {
        public string GetData(int value)
        {
            return string.Format("You entered: {0}", value);
        }


        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, UriTemplate = "register/user/{fname}/{lname}/{phonenum}/{email}/{password}")]
        public string registerUser(string fname, string lname, string phonenum, string email, string password)
        {
            OperationContext context = OperationContext.Current;
            MessageProperties messageProperties = context.IncomingMessageProperties;
            RemoteEndpointMessageProperty endpointProperty =
            messageProperties[RemoteEndpointMessageProperty.Name] as RemoteEndpointMessageProperty;

            try
            {
                SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["dbString"].ConnectionString);
                conn.Open();

                int userid = 1;


                string username = email;
                string role = "user";

                SqlCommand cmd = new SqlCommand("INSERT INTO USERS VALUES ('" + userid + "','" + username + "','" + password + "','" + fname + "','" + lname + "','" + email + "','" + phonenum + "','" + role + "')", conn);

                cmd.ExecuteNonQuery();
                cmd.Dispose();

                conn.Close();
                return "success";
            }
            catch (Exception e)
            {
                e.GetBaseException();

            }
            return "issue";
        }

        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, UriTemplate = "tripadd/{userid}/{tripname}/{startdate}/{enddate}/{placeid}")]
        public string tripAdd(string userid, string tripname, string startdate, string enddate, string placeid)
        {
            OperationContext context = OperationContext.Current;
            MessageProperties messageProperties = context.IncomingMessageProperties;
            RemoteEndpointMessageProperty endpointProperty =
            messageProperties[RemoteEndpointMessageProperty.Name] as RemoteEndpointMessageProperty;

            try
            {
                SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["dbString"].ConnectionString);
                conn.Open();

                int tripid = 1;
            
                SqlCommand cmd = new SqlCommand("INSERT INTO MYTRIPS VALUES ('" + userid + "','" + tripid + "','" + tripname + "','" + startdate + "','" + enddate + "','" + placeid + "')", conn);

                cmd.ExecuteNonQuery();
                cmd.Dispose();
                conn.Close();
                return "success";
            }
            catch (Exception e)
            {
                e.GetBaseException();

            }
            return "issue";
        }

        
        [WebInvoke(UriTemplate = "/registerUser",
                RequestFormat = WebMessageFormat.Json,
                ResponseFormat = WebMessageFormat.Json, Method = "POST")] 
        public string registerTLUser(Users user)
        {
            OperationContext context = OperationContext.Current;
            MessageProperties messageProperties = context.IncomingMessageProperties;
            RemoteEndpointMessageProperty endpointProperty =
            messageProperties[RemoteEndpointMessageProperty.Name] as RemoteEndpointMessageProperty;

            try
            {
                SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["dbString"].ConnectionString);
                conn.Open();

                int userid = 1;
                string username = user.email;
                string role = "user";

                SqlCommand cmd = new SqlCommand("INSERT INTO USERS VALUES ('" + userid + "','" + username + "','" + user.password + "','" + user.first_name + "','" + user.last_name + "','" + user.email + "','" + user.phoneno + "','" + role + "')", conn);

                cmd.ExecuteNonQuery();
                cmd.Dispose();

                conn.Close();
                return "success";
            }
            catch (Exception e)
            {
                e.GetBaseException();

            }
            return "issue";
        }


        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, UriTemplate = "login/user/{username}/{pwd}")]
        public Boolean loginUser(string username, string pwd)
        {
            System.Diagnostics.Debug.Write("tesing");
            try
            {
                SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["dbString"].ConnectionString);
                conn.Open();
                System.Diagnostics.Debug.Write("tesing");
                SqlCommand cmd = new SqlCommand("SELECT * from users where user_name = '" + username + "'", conn);
                SqlDataReader reader = cmd.ExecuteReader();
                System.Diagnostics.Debug.Write("tesing2");
                string password = "";
                while (reader.Read())
                {
                    password = password + reader["password"];
                    System.Diagnostics.Debug.Write(password);

                    if (password == pwd)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }

                }

                cmd.Dispose();
                conn.Close();
            }
            catch (Exception e)
            {
                e.GetBaseException();

            }
            return false;
        }

        [WebInvoke(Method = "GET", ResponseFormat = WebMessageFormat.Json, UriTemplate = "TripDetails/{userid}")]

        public string GetTripDetails(string userid)
        {
           
            TripObject[] trips = null;
            try
            {
                SqlConnection conn = new SqlConnection(ConfigurationManager.ConnectionStrings["dbString"].ConnectionString);
                conn.Open();
                SqlCommand cmd1 = new SqlCommand("SELECT * from mytrips where userid = '" + userid + "'", conn);
                
                SqlDataReader reader1 = cmd1.ExecuteReader();
                


                int i = 0;
               System.Data.DataTable dt = new System.Data.DataTable();
                dt.Load(reader1);
                int numofrows = dt.Rows.Count;
                SqlCommand cmd = new SqlCommand("SELECT * from mytrips where userid = '" + userid + "'", conn);
                SqlDataReader reader = cmd.ExecuteReader();
                trips = new TripObject[numofrows];
                while (reader.Read())
                {
                    trips[i] = new TripObject();
                    //trips[i].tripid = Convert.ToInt32( reader["tripid"].ToString());
                    trips[i].tripid = reader["tripid"].ToString();
                    trips[i].tripname = reader["tripname"].ToString();
                    trips[i].startdate = reader["startdate"].ToString();
                    trips[i].enddate = reader["enddate"].ToString();
                    trips[i].placeid = reader["placeid"].ToString();
                    trips[i].Latitude = reader["Latitude"].ToString();
                    trips[i].Longitude = reader["Longitude"].ToString();
                    i++;

                }

                cmd.Dispose();
                conn.Close();
            }
            catch (Exception e)
            {
                e.GetBaseException();

            }
            JavaScriptSerializer js1 = new JavaScriptSerializer();
            string json1 = js1.Serialize(trips);
            return json1;


        }


        public CompositeType GetDataUsingDataContract(CompositeType composite)
        {
            if (composite == null)
            {
                throw new ArgumentNullException("composite");
            }
            if (composite.BoolValue)
            {
                composite.StringValue += "Suffix";
            }
            return composite;
        }
    }
}
