  // SEED: 103 entries (original 100 + 3 new: Museum of Ice Cream, Eataly x2, Comfort Dental)
package tracker;

public class SeedData {
    public static final String SEED_MARKDOWN = 
        "| [MUSEUM OF ICE CREAM](https://www.museumoficecream.com/careers) | Show Ambassador (Weekends Only) | Retail / Customer Service | Chicago, IL | Applied | 10/15/2025 | LinkedIn |\n" +
        "| [Eataly](https://www.eataly.com/us_en/) | Cheesemonger / Salumi & Formaggi Artisan | Culinary / Retail | Chicago, IL | Applied | 10/15/2025 | LinkedIn |\n" +
        "| [Eataly](https://www.eataly.com/us_en/) | Cashier / Front End Associate – Seasonal | Retail / Service | Chicago, IL | Applied | 10/15/2025 | LinkedIn |\n" +
        "| [Comfort Dental Fairwood](https://www.comfortdental.com/) | Office Assistant | Admin / Office Support | Chicago, IL | Applied | 10/15/2025 | ZipRecruiter |\n" +
        "| [RealtyAds](https://www.realtyads.com/careers) | Junior Software Engineer | Software | Chicago, IL | Applied | 09/05/2025 | LinkedIn |\n" +
        "| [University of Chicago – Harris School of Public Policy](https://harris.uchicago.edu/) | Harris Social Impact Fellowship | Policy / Research | Chicago, IL | Applied | 09/05/2025 | LinkedIn |\n" +
        "| [SPAATECH, Inc](https://www.spaatech.com/) | Business Development Representative – Technology Solutions | Business / IT | Chicago, IL | Applied | 09/05/2025 | LinkedIn |\n" +
        "| [METTLER TOLEDO](https://www.mt.com/us/en/home/careers.html) | Software Engineer | Software | Changzhou Shi, China | Applied | 08/07/2025 | LinkedIn |\n" +
        "| [Supernova Companies](https://www.supernovacompanies.com/careers) | Entry Level Software Engineer | Software | Chicago, IL | Applied | 08/07/2025 | LinkedIn |\n" +
        "| [Supernova Companies](https://www.supernovacompanies.com/careers) | Data Analyst | Data | Chicago, IL | Applied | 08/07/2025 | LinkedIn |\n" +
        "| [Magnifact](https://www.magnifact.com/careers) | Data Analyst – SQL / ETL | Data | Chicago, IL | Applied | 07/30/2025 | LinkedIn |\n" +
        "| [Dominican University](https://www.dom.edu/hr/employment-opportunities) | Web Developer | Software / Web | Chicago, IL | Applied | 07/30/2025 | LinkedIn |\n" +
        "| [Capgemini America Inc.](https://www.capgemini.com/us-en/careers/) | Chicago Junior Developer | Software | Chicago, IL / New York, NY | Applied | 07/30/2025 | LinkedIn |\n" +
        "| [Oak Street Health](https://www.oakstreethealth.com/careers) | Medical Scribe | Healthcare / IT | Chicago, IL | Applied | 07/26/2025 | LinkedIn |\n" +
        "| [SFORCE IT](https://www.sforceit.com/) | Robotic Process Automation Developer | Automation / IT | Chicago, IL / Dallas, TX | Applied | 07/26/2025 | LinkedIn |\n" +
        "| [Sunixa Solutions Inc.](https://sunixasolutions.com/careers) | AI/ML Engineer | AI / Software | Remote | Applied | 07/26/2025 | LinkedIn |\n" +
        "| [SoftStandard Solutions](https://www.softstandard.com/careers) | Data Engineer – Python Development with AI/ML | Data / AI | Remote | Applied | 07/26/2025 | LinkedIn |\n" +
        "| [SoftStandard Solutions](https://www.softstandard.com/careers) | Python Developer | Software | Remote | Applied | 07/26/2025 | LinkedIn |\n" +
        "| [Fovus Corporation](https://fovus.com/careers) | Software Development Engineer | Software | Remote | Applied | 07/26/2025 | LinkedIn |\n" +
        "| [Washington State Department of Ecology](https://ecology.wa.gov/About-us/Jobs) | IT Business Analyst – Journey | IT / Analysis | Lacey, WA | Applied | 07/25/2025 | LinkedIn |\n" +
        "| [The UPS Store](https://www.theupsstore.com/careers) | Student Position — Rogers Park | Retail / Customer Service | Chicago, IL | Applied | 07/25/2025 | LinkedIn |\n" +
        "| [Blick Art Materials](https://www.dickblick.com/careers/) | Retail Sales Associate — Loop | Retail | Chicago, IL | Applied | 07/25/2025 | LinkedIn |\n" +
        "| [AA Software & Networking](https://aasoftware.com/) | Java Developer | Software | Remote | Applied | 07/25/2025 | LinkedIn |\n" +
        "| [Tubman Technologies](#) | Java Developer | Software | Remote | Applied | 07/25/2025 | LinkedIn |\n" +
        "| [Chicago Youth Centers](https://chicagoyouthcenters.org/careers) | STEAM Intern | Education / Nonprofit | Chicago, IL | Applied | 07/25/2025 | LinkedIn |\n" +
        "| [The Bridge Teen Center](https://thebridgeteencenter.org/) | Horticulture Plant Propagation Intern | Environmental / Education | Orland Park, IL | Applied | 07/25/2025 | LinkedIn |\n" +
        "| [American Red Cross](https://www.redcross.org/about-us/careers.html) | Fundraising & Strategy Intern | Nonprofit / Strategy | Remote | Applied | 07/25/2025 | LinkedIn |\n" +
        "| [Momotaro](https://www.bokagrp.com/careers) | Food Runner | Service | Chicago, IL | Applied | 08/29/2025 | Indeed |\n" +
        "| [Wakamono](#) | Food Runner | Service | Chicago, IL | Applied | 08/29/2025 | Indeed |\n" +
        "| [My Net Security, Inc.](#) | IT Technician | IT Support | Chicago, IL | Applied | 09/04/2025 | Indeed |\n" +
        "| [Taylor Farms Illinois](https://www.taylorfarms.com/careers/) | Label Room Technician | Manufacturing / Ops | Chicago, IL | Applied | 09/04/2025 | Indeed |\n" +
        "| [Creative Tech](#) | IT Field Technician | IT Support | Skokie, IL | Not Selected | 09/04/2025 | Indeed |\n" +
        "| [Fernwood Property Mgmt](#) | Maintenance Admin Intern | Admin / Operations | Chicago, IL | Applied | 09/04/2025 | Indeed |\n" +
        "| [Trump Intl. Hotel Chicago](https://www.trumphotels.com/chicago/careers) | Security Intern | Security / Ops | Chicago, IL | Applied | 09/04/2025 | Indeed |\n" +
        "| [Green Paws Chicago](https://greenpawschicago.com/) | Dog Walker & Pet Sitter | Animal Care | Chicago, IL | Applied (closed) | 09/04/2025 | Indeed |\n" +
        "| [AESLIN Pup Hub](#) | Dog Daycare & Boarding Handler | Animal Care | Chicago, IL | Applied (closed) | 09/05/2025 | Indeed |\n" +
        "| [PAWS Chicago](https://www.pawschicago.org/about-us/careers) | Veterinary Assistant Apprentice | Animal Care | Chicago, IL | Applied | 08/29/2025 | Indeed |\n" +
        "| [PAWS Chicago](https://www.pawschicago.org/about-us/careers) | Adoption Services Associate | Animal Care / Service | Chicago, IL | Applied | 07/20/2025 | Indeed |\n" +
        "| [Ravenswoof](#) | Pet Bather | Animal Care | Chicago, IL | Applied (closed) | 09/07/2025 | Indeed |\n" +
        "| [Bark Bark Club](https://www.barkbarkclub.com/) | Dog Daycare Attendant (Full-Time) | Animal Care | Chicago, IL | Not Selected | 09/23/2025 | Indeed |\n" +
        "| [Bark Bark Club](https://www.barkbarkclub.com/) | Dog Daycare Attendant (Part-Time) | Animal Care | Chicago, IL | Applied (closed) | 04/18/2025 | Indeed |\n" +
        "| [Lawrence Fish Market](#) | Sushi Chef Assistant | Culinary | Chicago, IL | Applied (closed) | 07/20/2025 | Indeed |\n" +
        "| [Eataly North America](https://www.eataly.com/us_en/) | Fishmonger — Raw Bar & Oyster | Culinary | Chicago, IL | Applied (closed) | 07/20/2025 | Indeed |\n" +
        "| [City Winery Chicago](https://citywinery.com/chicago) | Service Assistant | Service | Chicago, IL | Applied (closed) | 07/20/2025 | Indeed |\n" +
        "| [Cadinho Bakery & Cafe](#) | Food Service Associate | Food Service | Chicago, IL | Applied (closed) | 07/20/2025 | Indeed |\n" +
        "| [Montelimar Bread Co.](#) | Farmers Market Vendor | Food Service | Evanston, IL | Applied (closed) | 07/20/2025 | Indeed |\n" +
        "| [Anti-Cruelty Society](https://anticruelty.org/careers) | Animal Care Specialist | Animal Care | Chicago, IL | Applied (closed) | 07/20/2025 | Indeed |\n" +
        "| [Fire Hydrant Pet Sitting Co.](https://www.firehydrantpetsitting.com/) | Overnight Pet Care Specialist | Animal Care | Chicago, IL | Not Selected | 07/20/2025 | Indeed |\n" +
        "| [Thornton Tomasetti](https://www.thorntontomasetti.com/careers) | IT Support Technician | IT Support | Chicago, IL | Applied (closed) | 07/20/2025 | Indeed |\n" +
        "| [Kids STEM Studio](https://kidsstemstudio.com/) | After School Instructor | Education / Tech | Evanston, IL | Not Selected | 05/16/2025 | Indeed |\n" +
        "| [Sweet Rabbit Bakery](#) | Part-Time FOH | Service | Chicago, IL | Not Selected | 05/08/2025 | Indeed |\n" +
        "| [Fusion92](https://www.fusion92.com/careers) | Internship Program | Marketing / Data | Chicago, IL | Applied (closed) | 05/08/2025 | Indeed |\n" +
        "| [APCIA](https://www.apci.org/) | IT Systems Support Intern | IT Support | Chicago, IL | Applied (closed) | 05/08/2025 | Indeed |\n" +
        "| [C4 Chicago](https://www.c4chicago.org/) | Development Intern | Nonprofit / Dev | Chicago, IL | Applied (closed) | 05/08/2025 | Indeed |\n" +
        "| [Lincoln Park Art Gallery](#) | Art Gallery Assistant | Arts / Admin | Chicago, IL | Applied (closed) | 07/20/2025 | Indeed |\n" +
        "| [Advance Spine Rehab Center](#) | IT Personnel — As Needed | IT Support | Chicago, IL | Applied (closed) | 07/20/2025 | Indeed |\n" +
        "| [Robert Half](https://www.roberthalf.com/jobs) | Desk Support Technician | IT Support | Chicago, IL | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [Robert Half](https://www.roberthalf.com/jobs) | Part-Time Admin Assistant | Admin | Chicago, IL | Applied | 10/03/2025 | LinkedIn |\n" +
        "| [Robert Half](https://www.roberthalf.com/jobs) | Medical Coder | Healthcare / IT | Chicago, IL | Applied | 10/03/2025 | LinkedIn |\n" +
        "| [Robert Half](https://www.roberthalf.com/jobs) | Azure Data Architect | Cloud / Data | Remote | Applied | 10/03/2025 | LinkedIn |\n" +
        "| [Robert Half](https://www.roberthalf.com/jobs) | IT Site Discovery & Infrastructure Coordinator | IT Infrastructure | Chicago, IL | Applied | 10/03/2025 | LinkedIn |\n" +
        "| [Robert Half](https://www.roberthalf.com/jobs) | Help Desk Analyst II | IT Support | Chicago, IL | Applied | 10/03/2025 | LinkedIn |\n" +
        "| [Robert Half](https://www.roberthalf.com/jobs) | Desktop Support Analyst | IT Support | Chicago, IL | Applied | 10/03/2025 | LinkedIn |\n" +
        "| [Robert Half](https://www.roberthalf.com/jobs) | Help Desk Analyst | IT Support | Chicago, IL | Applied | 10/03/2025 | LinkedIn |\n" +
        "| [Optimum Healthcare IT](https://www.optimumhit.com/careers) | Entry-Level Healthcare IT Analyst | IT / Healthcare | Remote | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [GreenLoop IT Solutions](https://www.greenloopit.com/careers) | IT Support Specialist | IT Support | Remote | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [Pimlico Enterprises](https://www.pimlicoenterprises.com/careers) | Data Analyst Intern | Data | Remote | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [Kemon Marketing](https://www.kemonmarketing.com/careers) | Prompt Engineer | AI / Tech | Remote | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [Musing AI](https://www.musing.ai/careers) | Software Engineer Intern | Software | Remote | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [Reverb](https://reverb.com/jobs) | IT Support Specialist | IT Support | Chicago, IL | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [RKON](https://www.rkon.com/careers) | Analyst (Tier I) — End User Engineering | IT Support | Chicago, IL | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [Fairlife](https://www.fairlife.com/careers) | Data Science Intern | Data | Chicago, IL | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [Datadog](https://www.datadoghq.com/careers/) | Senior Enterprise IT Support Tech | IT Support | Chicago, IL | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [Datadog](https://www.datadoghq.com/careers/) | Internship | Software / Data | Chicago, IL | Applied | 09/06/2025 | LinkedIn |\n" +
        "| [Datadog](https://www.datadoghq.com/careers/) | Product Manager Intern | Product | Chicago, IL | Rejected | 09/10/2025 | LinkedIn |\n" +
        "| [Capgemini (France)](https://www.capgemini.com/careers/) | Apprenticeship | Software | France | Rejected | 09/08/2025 | LinkedIn |\n" +
        "| [Capgemini](https://www.capgemini.com/careers) | Business Analyst | Business / IT | Remote | Applied | 09/01/2025 | LinkedIn |\n" +
        "| [Mercor](https://www.mercor.io/) | English Support | IT / Support | Remote | Applied | 09/19/2025 | LinkedIn |\n" +
        "| [Mercor](https://www.mercor.io/) | Enterprise Cloud Domain Support | Cloud / IT | Remote | Applied | 09/19/2025 | LinkedIn |\n" +
        "| [Mercor](https://www.mercor.io/) | Intelligent Identity Engineer | Security / IT | Remote | Applied | 09/19/2025 | LinkedIn |\n" +
        "| [Mercor](https://www.mercor.io/) | Software Eng. Code Review (Rust) | Software | Remote | Applied | 09/19/2025 | LinkedIn |\n" +
        "| [Mercor](https://www.mercor.io/) | Data Engineer | Data | Remote | Applied | 09/19/2025 | LinkedIn |\n" +
        "| [RWE](https://www.rwe.com/careers) | IT Graduate Program | IT / Graduate | Remote | Rejected | 09/17/2025 | LinkedIn |\n" +
        "| [Morningstar](https://www.morningstar.com/careers) | Development Program for Technologists | Software / Data | Chicago, IL | Applied | 09/17/2025 | LinkedIn |\n" +
        "| [Epsilon](https://www.epsilon.com/us/about-us/careers) | New Grad Program | Software | Chicago, IL | Applied | 09/17/2025 | LinkedIn |\n" +
        "| [AllJoined](https://alljoined.com) | Software Engineering Research Intern | Software / Research | Remote | Applied | 10/02/2025 | LinkedIn |\n" +
        "| [TDS](https://tdstelecom.com/careers) | GIS Intern | GIS / Environmental | Remote | Applied | 10/13/2025 | LinkedIn |\n" +
        "| [LeoLabs](https://www.leolabs.space/careers) | Electrical Engineer | Engineering | Remote | Applied | 10/13/2025 | LinkedIn |\n" +
        "| [Council of International Programs Chicago](https://www.cipchicago.org/) | Web Designer / Developer Intern | Nonprofit / Web | Remote / Chicago, IL / Bensenville, IL | Applied | 07/20/2025 | Handshake |\n" +
        "| [Wesco](https://www.wesco.com/careers) | IT Development Program (2026) | IT / Graduate | Hybrid / Glenview, IL / Pittsburgh, PA | Applied | 07/20/2025 | Handshake |\n" +
        "| [DataCapable](https://www.datacapable.com/careers) | Software Engineer | Software | Remote / Chicago, IL | Applied | 07/20/2025 | Handshake |\n" +
        "| [GenieAI](https://www.genieai.co/) | Software Engineer | AI / Software | Remote | Applied | 07/20/2025 | Handshake |\n" +
        "| [Northwestern Mutual Chicagoland](https://chicagoland.nm.com/careers.htm) | Financial Advisor | Finance / Insurance | Onsite / Chicago, IL | Applied | 07/09/2025 | Handshake |\n" +
        "| [Blue Cross Blue Shield (HCSC)](https://jobs.hcsc.com/) | Associate Systems Analyst | Healthcare / IT | Hybrid / Chicago, IL | Applied | 06/27/2025 | Handshake |\n" +
        "| [Epic Systems Corporation](https://careers.epic.com/) | Software Developer | Software | Onsite / Verona, WI | Applied | 06/27/2025 | Handshake |\n" +
        "| [Belay Diagnostics](https://belaydiagnostics.com/careers) | Intern, Technical Project & Systems Management | Biotech / IT | Hybrid / Chicago, IL | Applied | 06/26/2025 | Handshake |\n" +
        "| [Siemens Digital Industries Software](https://www.siemens.com/global/en/company/jobs.html) | Software Development Intern | Software | Hybrid / Marlborough, MA / Huntsville, AL | Applied | 06/26/2025 | Handshake |\n" +
        "| [Vantero](https://vantero.ai/) | Software Engineering / LLM Intern | AI / Software | Remote | Applied | 06/26/2025 | Handshake |\n" +
        "| [Catholic Extension](https://www.catholicextension.org/about/careers/) | AI Data Strategy Intern | AI / Nonprofit | Onsite / Chicago, IL | Declined | 06/26/2025 | Handshake |\n" +
        "| [Fynite Corp.](https://fynite.ai/careers) | Data Scientist | Data | Remote / U.S. | Applied | 06/26/2025 | Handshake |\n" +
        "| [Fynite Corp.](https://fynite.ai/careers) | Data Engineer | Data | Remote / U.S. | Declined | 06/26/2025 | Handshake |\n" +
        "| [Google](https://careers.google.com/students/) | Student Researcher (BS/MS) | Research / Software | Hybrid / Ann Arbor, MI / Austin, TX | Applied | 05/12/2025 | Handshake |\n" +
        "| [Blueera Technologies, Inc.](https://blueeratech.com/careers) | DevOps Engineer | Software / IT | Remote / Indiana / TX | Applied | 05/07/2025 | Handshake |\n" +
        "| [The Public Interest Network Creative Team](https://publicinterestnetwork.org/jobs/) | Web & Digital Content Intern | Communications / Web | Remote / Chicago, IL / Boston, MA | Applied | 05/07/2025 | Handshake |\n" +
        "| [DataCapable](https://www.datacapable.com/careers) | Software Engineer | Software | Remote / Chicago, IL | Applied | 07/20/2025 | Handshake |\n" +
        "| [GenieAI](https://www.genieai.co/) | Software Engineer | AI / Software | Remote | Applied | 07/20/2025 | Handshake |\n" +
        "| [Northwestern Mutual Chicagoland](https://chicagoland.nm.com/careers.htm) | Financial Advisor | Finance / Insurance | Onsite / Chicago, IL | Applied | 07/09/2025 | Handshake |\n" +
        "| [Blue Cross Blue Shield (HCSC)](https://jobs.hcsc.com/) | Associate Systems Analyst | Healthcare / IT | Hybrid / Chicago, IL | Applied | 06/27/2025 | Handshake |\n" +
        "| [Epic Systems Corporation](https://careers.epic.com/) | Software Developer | Software | Onsite / Verona, WI | Applied | 06/27/2025 | Handshake |\n" +
        "| [Belay Diagnostics](https://belaydiagnostics.com/careers) | Intern, Technical Project & Systems Management | Biotech / IT | Hybrid / Chicago, IL | Applied | 06/26/2025 | Handshake |\n" +
        "| [Siemens Digital Industries Software](https://www.siemens.com/global/en/company/jobs.html) | Software Development Intern | Software | Hybrid / Marlborough, MA / Huntsville, AL | Applied | 06/26/2025 | Handshake |\n" +
        "| [Vantero](https://vantero.ai/) | Software Engineering / LLM Intern | AI / Software | Remote | Applied | 06/26/2025 | Handshake |\n" +
        "| [Catholic Extension](https://www.catholicextension.org/about/careers/) | AI Data Strategy Intern | AI / Nonprofit | Onsite / Chicago, IL | Declined | 06/26/2025 | Handshake |\n" +
        "| [Fynite Corp.](https://fynite.ai/careers) | Data Scientist | Data | Remote / U.S. | Applied | 06/26/2025 | Handshake |\n" +
        "| [Fynite Corp.](https://fynite.ai/careers) | Data Engineer | Data | Remote / U.S. | Declined | 06/26/2025 | Handshake |\n" +
        "| [Google](https://careers.google.com/students/) | Student Researcher (BS/MS) | Research / Software | Hybrid / Ann Arbor, MI / Austin, TX | Applied | 05/12/2025 | Handshake |\n" +
        "| [Blueera Technologies, Inc.](https://blueeratech.com/careers) | DevOps Engineer | Software / IT | Remote / Indiana / TX | Applied | 05/07/2025 | Handshake |\n" +
        "| [The Public Interest Network Creative Team](https://publicinterestnetwork.org/jobs/) | Web & Digital Content Intern | Communications / Web | Remote / Chicago, IL / Boston, MA | Applied | 05/07/2025 | Handshake |";
}